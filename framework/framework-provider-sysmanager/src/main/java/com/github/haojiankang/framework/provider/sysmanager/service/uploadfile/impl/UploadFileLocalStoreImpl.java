package com.ghit.framework.provider.sysmanager.service.uploadfile.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ghit.framework.commons.utils.FileUtils;
import com.ghit.framework.commons.utils.IdentityHelper;
import com.ghit.framework.commons.utils.Page;
import com.ghit.framework.commons.utils.bean.ObjectUtils;
import com.ghit.framework.commons.utils.lang.MathUtils;
import com.ghit.framework.commons.utils.lang.StringUtil;
import com.ghit.framework.commons.utils.security.model.IUser;
import com.ghit.framework.provider.sysmanager.api.model.po.sysmgr.Configuration;
import com.ghit.framework.provider.sysmanager.api.model.po.uploadfile.UploadFile;
import com.ghit.framework.provider.sysmanager.api.service.uploadfile.UploadFileService;
import com.ghit.framework.provider.sysmanager.api.supports.DataOper;
import com.ghit.framework.provider.sysmanager.dao.uploadfile.FileDao;
import com.ghit.framework.provider.sysmanager.supports.service.BaseServiceImpl;
import com.ghit.framework.provider.sysmanager.supports.sysmgr.SharpSysmgr;
import com.ghit.framework.provider.utils.PS;
import com.ghit.framework.provider.utils.hibernate.BaseDao;

@Service
public class UploadFileLocalStoreImpl extends BaseServiceImpl<UploadFile,UploadFile> implements UploadFileService {
    Log log = LogFactory.getLog(UploadFileLocalStoreImpl.class);

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public UploadFile saveFile(UploadFile t,IUser user) {
        boolean blReturn = false;
        UploadFile ufReturn = null;
    	//检查是否已存在同一文件
    	UploadFile old = getPrevious(t);
        if(null != old) {
            //如果文件存在并且为完整状态就不再上传文件
            if(StringUtil.eq(old.getFlag(), old.FLAG_COMPLETE)) {
            	old.setFirst(false);
                return old;
            }
            if(null != t.getFlag()) old.setFlag(t.getFlag()); 
            if(null != t.getRange()) old.setRange(t.getRange());
            if(null != t.getBytes()) old.setBytes(t.getBytes());
            if(null != t.getStream()) old.setStream(t.getStream()); 
            blReturn = updateFile(old,user);
            ufReturn = old;
            ufReturn.setFirst(false);
        } else {
            blReturn = insertFile(t,user);
            ufReturn = t;
            ufReturn.setFirst(true);
        }
        return blReturn ? ufReturn : null;
    }

    @Override
    public UploadFile[] selectFilesByIds(String... ids) {
        if (ObjectUtils.isEmpty(ids))
            return null;
        UploadFile uf = null;
        List<UploadFile> files = new ArrayList<UploadFile>();
        for (String id : ids) {
        	uf = ObjectUtils.isEmpty(id) ? null : super.findById(new UploadFile(id));
            if(null != uf) files.add(uf);
        }
        return files.isEmpty() ? null : files.toArray(new UploadFile[files.size()]);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean del(UploadFile t) {
        UploadFile old = super.findById(t);
        if (null == old || ObjectUtils.isEmpty(old.getPath()))
            return false;
        File dest = new File(old.getPath());
        return (dest.exists() && dest.isFile()) ? (dest.delete() && super.del(t)) : false;
    }

    @Override
    public File getPhysicalFile(UploadFile t) {
        if (null == t || ObjectUtils.isEmpty(t.getPath()))
            return null;
        File f = new File(t.getPath());
        return f.exists() ? f : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UploadFile> getFileByConditions(Map<String, Object> conditions) {
        Page page = new Page();
        page.setRows(Integer.MAX_VALUE);
        page.setConditions(conditions);
        dao.findPage(page);
        return (List<UploadFile>) page.getResult();
    }

    @Override
    @Transactional(propagation=Propagation.SUPPORTS)
	public UploadFile getUploadFileActualMD5(String fid) {
	    UploadFile uf = ObjectUtils.isEmpty(fid) ? null : super.findById(new UploadFile(fid));
	    File fileDest = (null == uf || ObjectUtils.isEmpty(uf.getPath())) ? null : new File(uf.getPath());
        String md5 = null != fileDest ? FileUtils.getFileMD5(fileDest) : null;
        if(null != uf) uf.setMd5(md5);
        return uf;
	}

	private boolean insertFile(UploadFile t,IUser user) {
        if(ObjectUtils.isEmpty(t.getSuffix())) t.setSuffix(FileUtils.getExtension(t.getFileName()));//文件后缀

      //生成文件ID，当前命名策略仅是为了方便维护人员检查文件，存在多应用服务器会出现冲突问题；解决方式是采用uuid命名
        StringBuffer sbf = new StringBuffer(1000);
        IdentityHelper rdm = new IdentityHelper();
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
        sbf.append(sdf.format(gc.getTime())).append("-").append(rdm.randomString(4));
        t.setId(sbf.toString());
        t.setCreateUser(user.getId());
        t.setCreateTime(new Date());
        t.setCreateUserType(user.getUserType().toString());

      //文件名：文件ID + 文件后缀
        sbf.delete(0, sbf.length());
        sbf.append(t.getId()).append(".").append(StringUtil.trimToEmpty(t.getSuffix()));
        String strFileName = sbf.toString();

      //文件访问url
        sbf.delete(0, sbf.length());
        File currentPath = getCurrentPath();
        sbf.append(getBaseUrl()).append(getPathName(currentPath)).append("/").append(strFileName);
        t.setFileUrl(sbf.toString());

        //存入缓存
        if(!ObjectUtils.isEmpty(t.getMd5())) {
        	try {
        		Cache cache = getCache();
        		if (cache != null) {
    	        	Cache.ValueWrapper value = cache.putIfAbsent(t.getMd5(), t);
    	        	//当已有缓存对象存在说明当前文件块不应该insert，应当update
    	        	if (value != null) return false;
    	        }
            } catch (Exception e) {
                log.error(t.getMd5(), e);
                return false;
	        }
        }

        //写入磁盘文件
        sbf.delete(0, sbf.length());
        sbf.append(currentPath.getAbsolutePath()).append(File.separator).append(strFileName);
        File dest = makeFile(new File(sbf.toString()), null != t.getBytes() ? t.getBytes() : t.getStream(),
                null != t.getFileSize() ? t.getFileSize().longValue() : 0,
                null != t.getRange() ? t.getRange().longValue() : 0);
        if (null == dest) return false;
        checkMD5(t, dest);//检查md5
        try {//存放物理路径
            t.setPath(dest.getCanonicalPath());
        } catch (IOException e) {
            t.setPath(dest.getAbsolutePath());
        }
        return super.insert(t);
    }

    private boolean updateFile(UploadFile t,IUser user) {
        if (null == t || ObjectUtils.isEmpty(t.getPath()))
            return false;
        File dest = makeFile(new File(t.getPath()), null != t.getBytes() ? t.getBytes() : t.getStream(),
                t.getFileSize(), null != t.getRange() ? t.getRange().longValue() : 0);
        if (null == dest) return false;

        checkMD5(t, dest);//检查md5
        t.setUpdateUser(user.getId());
        t.setUpdateTime(new Date());
        t.setUpdateUserType(user.getUserType().toString());
        return super.update(t);
    }

    private UploadFile getPrevious(UploadFile uf) {
		UploadFile old = null;
		//先检查缓存
		if(!ObjectUtils.isEmpty(uf.getMd5())) {
	        try {
	        	Cache cache = getCache();
	        	if (cache != null) {
	        		Cache.ValueWrapper value = cache.get(uf.getMd5());
	        		if (value != null) old = (UploadFile) value.get();
	        	}
	        } catch (Exception e) {
	            log.error(uf.getMd5(), e);
	        }
	    }
		//再检查数据库
		if (old == null) {
			old = ObjectUtils.isEmpty(uf.getId()) ? dao.findFileByMD5(uf) : super.findById(uf);
		}
		return old;
	}

	/**
     * 上传完毕的检查md5
     */
    private void checkMD5(UploadFile t, File fileDest) {
        if (ObjectUtils.isEmpty(t.getFlag()) || !fileDest.exists()
                || !t.getFlag().equalsIgnoreCase(t.FLAG_COMPLETE)) return;
        String md5 = FileUtils.getFileMD5(fileDest);
        if(ObjectUtils.isEmpty(t.getMd5())) {//client没有提交md5则认为上传完整
            t.setMd5(md5);
        } else {//client提交md5则校验文件是否上传完整
            t.setFlag(md5.equalsIgnoreCase(t.getMd5()) ? t.FLAG_COMPLETE : t.FLAG_INCOMPLETE);
        }
    }

    /**
     * makeId:获取目标目录名。
     * 
     * @author admin
     * @param path
     * @return
     * @since JDK 1.7
     */
    private String getPathName(File path) {
        String strPath = path.getAbsolutePath();
        if (strPath.endsWith(File.separator))
            strPath = StringUtils.left(strPath, strPath.length() - File.separator.length());
        int intIdx = StringUtils.lastIndexOf(strPath, File.separator);
        return StringUtils.right(strPath, strPath.length() - intIdx - 1);
    }

    /**
     * writeFile:生成物理存储文件.
     * 
     * @author admin
     * @param id
     * @param suffix
     * @param content
     * @return
     * @since JDK 1.7
     */
    private File makeFile(File destFile, Object content, long fileSize, long rangePos) {
        if (null == destFile || destFile.isDirectory())
            return null;

        // 新文件需分配空间
        if (!destFile.exists() && fileSize > 0 && !FILE_NAME_REGISTRY.containsKey(destFile.getName())) {
            try {
                int newValue = COUNTER_VAL.getAndIncrement();
                Integer oldValue = FILE_NAME_REGISTRY.putIfAbsent(destFile.getName(), newValue);
                if (null == oldValue || newValue == oldValue.intValue())
                    FileUtils.createFile(destFile, fileSize);
            } finally {
                FILE_NAME_REGISTRY.remove(destFile.getName());
            }
        }

        // 没有实际内容时，仅用来生成空文件，可以为后续分块上传文件做预处理
        if (null == content)
            return destFile;

        // 写入文件内容
        boolean blReturn = false;
        if (content instanceof InputStream) {
            blReturn = FileUtils.writeFile(destFile, (InputStream) content, rangePos);
        } else if (content instanceof byte[]) {
            blReturn = FileUtils.writeFile(destFile, (byte[]) content, rangePos);
        } else if (content instanceof File) {
            blReturn = FileUtils.writeFile(destFile, (File) content, rangePos);
        } else if (content instanceof String) {
            blReturn = FileUtils.writeFile(destFile, new File(String.valueOf(content)), rangePos);
        } else if (content instanceof FileInputStream) {
            blReturn = FileUtils.writeFile(destFile, (FileInputStream) content, rangePos);
        }
        return blReturn ? destFile : null;
    }

    @SuppressWarnings("unused")
    @Deprecated
    private File searchFile(File destPath, String fileid) {
        List<String> lst = new ArrayList<String>(1);
        lst.add(fileid);
        File[] files = this.searchFile(destPath, lst);
        if (files != null && files.length > 0)
            return files[0];
        return null;
    }

    @Deprecated
    private File[] searchFile(File destPath, List<String> fileids) {
        if (fileids == null || fileids.size() < 1)
            return null;
        StringBuffer sbf = new StringBuffer(1000);
        List<String> lstTmp = new ArrayList<String>(fileids.size());
        for (String id : fileids) {
            sbf.delete(0, sbf.length());
            sbf.append(id).append(".");
            lstTmp.add(sbf.toString());
        }
        final String[] arryFileids = lstTmp.toArray(new String[lstTmp.size()]);
        File[] files = destPath.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                for (String id : arryFileids) {
                    if (name.startsWith(id))
                        return true;
                }
                return false;
            }
        });
        return files;
    }

    /**
     * getCurrentPath:获取当前文件存放目录。 搜索文件存储根目录的直接子目录，选择未满（文件数未达上
     * 限）的子目录存放文件，所有子目录均饱和时，新建子目录。
     *
     * @author admin
     * @return
     * @since JDK 1.7
     */
    private File getCurrentPath() {
        File baseDirectory = new File(getBasePath());

        // 获取可用子目录
        File[] dirs = null;
        try {
            FileScanner filter = new FileScanner(getMaxNum());
            CURRENT_FILE_PATH = null;
            dirs = baseDirectory.listFiles(filter);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (null != msg && msg.trim().length() > 0) {
                File f = new File(msg.trim());
                if (f.exists() && f.isDirectory())
                    return f;
            }
        }
        if (null != dirs && dirs.length > 0)
            return dirs[0];
        if (null != CURRENT_FILE_PATH)
            return CURRENT_FILE_PATH;

        // 新建子目录
        int intPath = 0;
        String[] names = baseDirectory.list();
        if (null != names && names.length > 0) {
            List<Integer> numbers = new ArrayList<Integer>();
            for (String n : names) {
                intPath = MathUtils.toInt(n, -1);
                if (intPath < 0)
                    continue;
                numbers.add(intPath);
            }
            if (numbers.size() > 0) {
                intPath = -1;
                int[] sortNumbers = ObjectUtils.integerList2IntArray(numbers);
                Arrays.sort(sortNumbers);
                for (int i : sortNumbers) {
                    if ((i - intPath) > 1) {
                        intPath = intPath + 1;
                        break;
                    } else {
                        intPath = i;
                    }
                }
            }
        }
        if (intPath < 0)
            intPath = 0;

        String fielpath = (new StringBuffer(2000)).append(getBasePath()).append(intPath).toString();
        FileUtils.createDirectory(fielpath);
        return new File(fielpath);
    }

    private void setCurrentPath(File searched) {
        CURRENT_FILE_PATH = searched;
    }

    private class FileScanner implements FileFilter {
        boolean blFound = false;
        int maxPerPath = 1024 * 10;

        public FileScanner(int maxPerPath) {
            this.maxPerPath = maxPerPath;
        }

        @Override
        public boolean accept(File path) {
            if (true == blFound || !path.isDirectory())
                return false;
            String[] ss = path.list();
            if (ss == null || ss.length < maxPerPath) {
                blFound = true;
                setCurrentPath(path);
                throw new RuntimeException(path.getAbsolutePath());
            } else
                return false;
        }
    }

    private Integer MAX_PER_PATH = null;
    private String FILE_BASE_URL = null;
    private String FILE_BASE_PATH = null;
    private File CURRENT_FILE_PATH;
    private final AtomicInteger COUNTER_VAL = new AtomicInteger(0);
    private final ConcurrentMap<String, Integer> FILE_NAME_REGISTRY = new ConcurrentHashMap<String, Integer>();
    private final String CACHE_UPLOAD_FILE = "upload_file";//上传文件缓存区名称

    @Resource
    private FileDao dao;
    @Override
    public BaseDao<UploadFile, Serializable> getBaseDao() {
        return dao;
    }
    @Resource(name="cacheManager")
    private CacheManager cacheManager;

    private Cache getCache() {
        return (cacheManager == null || ObjectUtils.isEmpty(CACHE_UPLOAD_FILE)) ? null : cacheManager.getCache(CACHE_UPLOAD_FILE);
    }

    private int getMaxNum() {
        if (MAX_PER_PATH != null)
            return MAX_PER_PATH.intValue();
        MAX_PER_PATH = MathUtils.toInt(SharpSysmgr.getConfigValue("FileManage.MaxPerPath"), 1024);
        return MAX_PER_PATH.intValue();
    }

    public Boolean setMaxNum(Configuration config, DataOper oper) {
        if (oper.equals(DataOper.QUERY) || oper.equals(DataOper.REMOVE))
            return true;
        int maxNum = MathUtils.toInt(config.getConfigurationValue(), -1);
        if (maxNum < 100) {
            PS.message("参数值必须是大于100的整数！");
            return false;
        }
        MAX_PER_PATH = maxNum;
        return true;
    }

    private String getBaseUrl() {
        if (FILE_BASE_URL != null)
            return FILE_BASE_URL;
        FILE_BASE_URL = SharpSysmgr.getConfigValue("FileManage.BaseUrl");
        if (!FILE_BASE_URL.endsWith("/"))
            FILE_BASE_URL = FILE_BASE_URL + "/";
        return FILE_BASE_URL;
    }

    public Boolean setBaseUrl(Configuration config, DataOper oper) {
        if (oper.equals(DataOper.QUERY))
            return true;
        if (oper.equals(DataOper.REMOVE)) {
            PS.message("参数值不能被删除！");
            return false;
        }
        String baseUrl = config.getConfigurationValue();
        FILE_BASE_URL = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        return true;
    }

    private String getBasePath() {
        if (FILE_BASE_PATH != null)
            return FILE_BASE_PATH;
        FILE_BASE_PATH = SharpSysmgr.getConfigValue("FileManage.BasePath");
        if (!FILE_BASE_PATH.endsWith(File.separator))
            FILE_BASE_PATH = FILE_BASE_PATH + File.separator;
        return FILE_BASE_PATH;
    }

    public Boolean setBasePath(Configuration config, DataOper oper) {
        if (oper.equals(DataOper.QUERY))
            return true;
        if (oper.equals(DataOper.REMOVE)) {
            PS.message("参数值不能被删除！");
            return false;
        }
        String basePath = config.getConfigurationValue();
        FILE_BASE_PATH = basePath.endsWith(File.separator) ? basePath : basePath + File.separator;
        return true;
    }

}
