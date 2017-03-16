package com.github.haojiankang.framework.commons.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.haojiankang.framework.commons.utils.lang.StringUtil;
import com.github.haojiankang.framework.commons.utils.security.RSATools;

/**
 * 文件操作工具类
 * 
 * @author ren7wei
 *
 */
public class FileUtils {

    protected static final Log log = LogFactory.getLog(FileUtils.class);

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024 * 10;// 10M

    /**
     * 返回应用根目录 WEB-INF/classes
     * 
     * @return
     */
    public static File getAppRoot() {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource("");
            File classPath = new File(url.toURI());
            return classPath.getParentFile().getParentFile();
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * 返回应用根目录 WEB-INF/classes
     * 
     * @return
     */
    public static File getAppRoot(String path) {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource("");
            File classPath = new File(url.toURI());
            String strWEB = classPath.getParentFile().getParentFile().getAbsolutePath();
            return new File(strWEB.endsWith(File.separator) ? strWEB + path : strWEB + File.separator + path);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * 返回应用根目录 WEB-INF/classes
     * 
     * @return
     */
    public static File getClassRoot(String path) {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(File.separator + path);
            return new File(url.toURI());
        } catch (Exception e) {
            return null;
        }
    }

    public static Properties loadPropertie(String name) throws IOException {
        Properties properties = new Properties();
        File appRoot = getClassRoot("");
        File file = new File(appRoot, name);
        properties.load(new FileInputStream(file));
        return properties;
    }

    public static Properties loadProperties(String name) throws IOException {
        Properties properties = new Properties();
        File appRoot = getClassRoot("");
        File dir = new File(appRoot, name);
        if (dir.isDirectory()) {
            for (File item : dir.listFiles()) {
                if (item.isFile()) {
                    Properties tempProperties = loadPropertie(name + "/" + item.getName());
                    properties.putAll(tempProperties);
                }
            }
        }
        return properties;
    }

    public static String fileToString(File file, String encoding) throws IOException {
        if (file == null)
            return null;
        StringBuilder txt = new StringBuilder();
        InputStreamReader read = null;
        BufferedReader buffer = null;
        try {
            read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
            buffer = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = buffer.readLine()) != null) {
                txt.append(lineTxt);
                txt.append("\r\n");
            }
            read.close();
        } finally {
            if (read != null)
                read.close();
            if (buffer != null)
                buffer.close();

        }
        return txt.toString();
    }

	/**
	 * 从文件读取字节内容
	 * @param file 指定文件
	 * @param position 读取起始位置，默认0
	 * @param size 读取字节长度，默认起始位置到文件尾
	 * @return 文件字节内容
	 */
	public static byte[] readBytes(File file, Long position, Long size) {
		if (file.exists() == false) return null;

		FileChannel fc = null;
		RandomAccessFile raf = null;
        byte[] buff = new byte[DEFAULT_BUFFER_SIZE];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			raf = new RandomAccessFile(file, "r");
			fc = raf.getChannel();
			//计算读取位置和读取长度
			long skip = (null==position || position.longValue() < 0) ? 0 : position.longValue();
			long readSize = fc.size() - skip;
			if (readSize < 0) return null;
			readSize = (null==size || size.longValue() > readSize) ? readSize : (size.longValue() < 0 ? 0 : size.longValue());
			//使用内存文件映射，速度会快很多
			MappedByteBuffer mbb = fc.map(MapMode.READ_ONLY, skip, readSize);

			if (readSize <= DEFAULT_BUFFER_SIZE) {// 如果文件不大,可以选择一次性读取到数组
				mbb.get(buff, 0, (int)readSize);
				baos.write(buff, 0, (int)readSize);

			} else {// 如果文件内容很大,可以循环读取,计算应该读取多少次
		        long cycle = readSize / DEFAULT_BUFFER_SIZE;
		        int mode = (int)(readSize % DEFAULT_BUFFER_SIZE);
		        for (int i = 0; i < cycle; i++) {
		        	mbb.get(buff);// 每次读取DEFAULT_BUFFER_SIZE个字节
		            baos.write(buff);
		        }
		        if(mode > 0) {
		        	buff = new byte[mode];
		        	mbb.get(buff);
		        	baos.write(buff);
		        }
			}
			return baos.toByteArray();
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				if (fc != null) fc.close();
			} catch (Exception e) {}
			try {
				if (raf != null) raf.close();
			} catch (Exception f) {}
			try {
				if (baos != null) baos.close();
			} catch (IOException g) {}
		}
		return null;
	}

    /**
     * createFile:预分配文件所占的磁盘空间，磁盘中会创建一个指定大小的文件。
     *
     * @author admin
     * @param file
     *            目标文件
     * @param filesize
     *            文件大小
     * @since JDK 1.7
     */
    public static boolean createFile(File file, long filesize) {
        if (file.exists())
            return false;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            raf.setLength(filesize);
            return true;
        } catch (IOException e) {
            log.error(file.getAbsolutePath(), e);
        } finally {
            try {
                if (raf != null)
                    raf.close();
            } catch (IOException g) {
            }
        }
        return false;
    }

    /**
     * 将字节数组写入文件，支持多个线程并发调用。
     * 
     * @param dest
     *            目标文件
     * @param src
     *            要写入的字节内容
     * @param rangePos
     *            写入位置，小于0追加至文件尾，其他位置写入或覆盖。
     */
    public static boolean writeFile(File dest, byte[] src, long rangePos) {
        if (null == dest || null == src)
            return false;
        FileChannel fc = null;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(dest, "rw");
            // raf.seek(rangePos);
            // raf.write(fileBytes);
            fc = raf.getChannel();
            fc.write(ByteBuffer.wrap(src), rangePos < 0 ? fc.size() : rangePos);
            return true;
        } catch (IOException e) {
            log.error(dest.getAbsolutePath(), e);
        } finally {
            try {
                if (fc != null)
                    fc.close();
            } catch (Exception f) {
            }
            try {
                if (raf != null)
                    raf.close();
            } catch (IOException g) {
            }
        }
        return false;
    }

    /**
     * 将输入流写入文件，支持多个线程并发调用。
     * 
     * @param dest
     *            目标文件
     * @param src
     *            输入流
     * @param rangePos
     *            写入位置，小于0追加至文件尾，其他位置写入或覆盖。
     */
    public static boolean writeFile(File dest, InputStream src, long rangePos) {
        if (null == dest || null == src)
            return false;
        BufferedInputStream bis = null;
        RandomAccessFile raf = null;
        try {
            if (src.available() < 1)
                return false;
            bis = new BufferedInputStream(src);
            raf = new RandomAccessFile(dest, "rw");
            rangePos = rangePos < 0 ? raf.length() : rangePos;
            raf.seek(rangePos);
            int num = -1;
            byte[] arr = new byte[DEFAULT_BUFFER_SIZE];
            while ((num = bis.read(arr)) > 0) {
                raf.write(arr, 0, num);
                rangePos = rangePos + num;
                raf.seek(rangePos);
            }
            return true;
        } catch (IOException e) {
            log.error("", e);
        } finally {
            try {
                if (null != raf)
                    raf.close();
            } catch (IOException e) {
            }
            try {
                if (null != bis)
                    bis.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

    /**
     * 将输入文件流写入文件，支持多个线程并发调用。
     * 
     * @param dest
     *            目标文件
     * @param src
     *            输入文件流
     * @param rangePos
     *            写入位置，小于0追加至文件尾，其他位置写入或覆盖。
     */
    public static boolean writeFile(File dest, FileInputStream src, long rangePos) {
        if (null == dest || null == src)
            return false;
        RandomAccessFile raf = null;
        FileChannel fcIn = null;
        FileChannel fcOut = null;
        try {
            if (src.available() < 1)
                return false;
            raf = new RandomAccessFile(dest, "rw");
            fcOut = raf.getChannel();
            fcOut.position(rangePos < 0 ? fcOut.size() : rangePos);
            fcIn = src.getChannel();
            fcIn.transferTo(0, fcIn.size(), fcOut);
            return true;
        } catch (IOException e) {
            log.error("", e);
        } finally {
            try {
                if (null != fcIn)
                    fcIn.close();
            } catch (IOException e) {
            }
            try {
                if (null != fcOut)
                    fcOut.close();
            } catch (IOException e) {
            }
            try {
                if (null != raf)
                    raf.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

    /**
     * 将输入文件合并至目标文件，支持多个线程并发调用。
     * 
     * @param dest
     *            目标文件
     * @param src
     *            输入文件
     * @param rangePos
     *            写入位置，小于0追加至文件尾，其他位置写入或覆盖。
     */
    public static boolean writeFile(File dest, File src, long rangePos) {
        if (null == dest || null == src || !src.exists() || !src.isFile())
            return false;
        RandomAccessFile raf = null;
        FileInputStream fis = null;
        FileChannel fcIn = null;
        FileChannel fcOut = null;
        try {
            raf = new RandomAccessFile(dest, "rw");
            fcOut = raf.getChannel();
            fcOut.position(rangePos < 0 ? fcOut.size() : rangePos);
            fis = new FileInputStream(src);
            fcIn = fis.getChannel();
            fcIn.transferTo(0, fcIn.size(), fcOut);
            return true;
        } catch (IOException e) {
            log.error("", e);
        } finally {
            try {
                if (null != fcIn)
                    fcIn.close();
            } catch (IOException e) {
            }
            try {
                if (null != fis)
                    fis.close();
            } catch (IOException e) {
            }
            try {
                if (null != fcOut)
                    fcOut.close();
            } catch (IOException e) {
            }
            try {
                if (null != raf)
                    raf.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

    /**
     * 获取文件后缀
     * 
     * @param filePath
     *            文件路径
     * @return 文件后缀，不包含符号“.”
     */
    public static String getExtension(String filePath) {
        if (null == filePath || StringUtil.trimToEmpty(filePath).length() < 1)
            return null;
        int intPos = filePath.lastIndexOf(".");
        return (intPos > -1 && intPos < (filePath.length() - 1)) ? filePath.substring(intPos + 1) : "";
    }

    /**
     * getFileMD5:获取文件的md5值.
     * 
     * @author admin
     * @param file
     * @return
     * @since JDK 1.7
     */
    public static String getFileMD5(File file) {
        if (file.exists() == false)
            return null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] md5 = Sharp.digest(fis, "MD5");
            return new String(RSATools.encodeHex(md5));
        } catch (IOException | NoSuchAlgorithmException e) {
            log.error("", e);
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 拷贝文件
     * 
     * @param src
     *            原文件
     * @param dest
     *            目的文件
     * @return 是否成功
     */
    public static boolean copyFile(File src, File dest) {
        if (null == src || !src.isFile())
            return false;
        if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs())
            return false;
        if (dest.isFile() && dest.exists())
            dest.delete();

        FileChannel fcIn = null, fcOut = null;
        RandomAccessFile rafIn = null, rafOut = null;
        byte[] buff = new byte[DEFAULT_BUFFER_SIZE];

        try {
            rafIn = new RandomAccessFile(src, "r");
            rafOut = new RandomAccessFile(src, "rw");
            fcIn = rafIn.getChannel();
            fcOut = rafOut.getChannel();

            long fileSize = fcIn.size();
            MappedByteBuffer mbbIn = fcIn.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            MappedByteBuffer mbbOut = fcOut.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);

            if (fileSize <= DEFAULT_BUFFER_SIZE) {// 如果文件不大,可以选择一次性读取到内存
                mbbIn.get(buff, 0, (int) fileSize);
                mbbOut.put(buff, 0, (int) fileSize);

            } else {// 如果文件内容很大,可以循环读取,计算应该读取多少次
                long cycle = fileSize / DEFAULT_BUFFER_SIZE;
                int mode = (int) (fileSize % DEFAULT_BUFFER_SIZE);
                for (int i = 0; i < cycle; i++) {// 每次读取DEFAULT_BUFFER_SIZE个字节
                    mbbIn.get(buff, 0, DEFAULT_BUFFER_SIZE);
                    mbbOut.put(buff, 0, DEFAULT_BUFFER_SIZE);
                }
                if (mode > 0) {
                    buff = new byte[mode];
                    mbbOut.get(buff, 0, mode);
                    mbbOut.put(buff, 0, mode);
                }
            }
            return true;
        } catch (IOException e) {
            log.error("", e);
        } finally {
            try {
                if (fcOut != null)
                    fcOut.close();
            } catch (IOException e) {
            }
            try {
                if (fcIn != null)
                    fcIn.close();
            } catch (IOException e) {
            }
            try {
                if (rafIn != null)
                    rafIn.close();
            } catch (IOException e) {
            }
            try {
                if (rafOut != null)
                    rafOut.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

    /**
     * 移动文件
     * 
     * @param src
     *            原文件
     * @param dest
     *            目的文件
     * @return 是否成功
     */
    public static boolean moveFile(File src, File dest) {
        if (!copyFile(src, dest))
            return false;
        return src.delete();
    }

    /**
     * 新建目录
     * 
     * @param path
     *            待新建的目录
     */
    public static boolean createDirectory(String path) {
        File directory = new File(path);
        return directory.exists() ? true : directory.mkdirs();
    }

    /**
     * getFile:获取服务器上文件
     *
     * @author admin
     * @param relativePath
     * @return
     * @since JDK 1.7
     */
    public static File getFile(String relativePath) {
        StringBuffer sbf = new StringBuffer(1000);
        String path = null;
        try {
            path = getAppRoot().getCanonicalPath();
        } catch (IOException e) {
            path = getAppRoot().getAbsolutePath();
        }
        sbf.append(path).append(relativePath.startsWith(File.separator) ? "" : File.separator).append(relativePath);
        File f = new File(sbf.toString());
        return f.exists() ? f : null;
    }

    public static byte[] readFile(File src, long position, int length) {
        byte[] bs = null;
        FileInputStream in = null;
        FileChannel cin = null;
        try {
            in = new FileInputStream(src);
            cin = in.getChannel();
            bs = readFile(cin, position, length);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            try {
                if (cin != null)
                    cin.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
        return bs;
    }

    private static byte[] readFile(FileChannel cin, long position, int length) throws IOException {
        byte[] bs = null;
        ByteBuffer buffer = ByteBuffer.allocate(length);
        cin.read(buffer, position);
        bs = buffer.array();
        return bs;
    }

    public static String fileMd5(File src) {
        String md5 = null;
        FileInputStream in = null;
        FileChannel cin = null;
        try {
            long len = src.length();
            int step = 1024 * 8*10;
            int position = 0;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(src);
            cin = in.getChannel();
            byte[] bs = null;
            while (position < len) {
                step=(int) ((step+position)<len?step:(len-position));
                bs = readFile(cin, position, step);
                position += step;
                digest.update(bs);
            }
            byte[] digest2 = digest.digest();
            md5 = toHexString(digest2);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            try {
                if (cin != null)
                    cin.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
        return md5;

    }
    private static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    protected static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static String getFileExtension(String name) {
		if (name == null || name.trim().length() < 1) return null;
		int intPos =  name.lastIndexOf(".");
		if (intPos > -1 && intPos < (name.length() - 1))
			return name.substring(intPos + 1);
		return "";
	}

	public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        System.out.println(fileMd5(new File("E:\\GHOST\\C_BACKUP.GHO")));
        System.out.println(System.currentTimeMillis());
    }
}
