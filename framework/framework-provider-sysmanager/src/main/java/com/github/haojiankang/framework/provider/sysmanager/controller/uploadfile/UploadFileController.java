package com.github.haojiankang.framework.provider.sysmanager.controller.uploadfile;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.haojiankang.framework.provider.sysmanager.api.model.po.uploadfile.UploadFile;
import com.github.haojiankang.framework.provider.sysmanager.api.service.uploadfile.UploadFileService;
import com.github.haojiankang.framework.provider.sysmanager.api.supports.service.BaseService;
import com.github.haojiankang.framework.provider.sysmanager.controller.common.BaseController;

@Controller
@RequestMapping("/upload")
public class UploadFileController extends BaseController<UploadFile,UploadFile> {
    Log log = LogFactory.getLog(UploadFileController.class);

    @Resource
    private UploadFileService uploadFileService;
    @Override
    public BaseService<UploadFile,UploadFile> getBaseService() {
        return uploadFileService;
    }

  //Content-Type: multipart/form-data
//    @ResponseBody
//    @RequestMapping(value="files", method=RequestMethod.POST)
//    public Object saveFiles(@RequestParam(value="files", required=true) MultipartFile[] files) {
//        UploadResponse result = new UploadResponse();
//        try {
//            UploadFile uf = null;
//            for (MultipartFile item : files) {
//                if(item.isEmpty()) {
//                    result.add(item.getOriginalFilename(), "file is empty!");
//                    continue;
//                }
//
//                uf = new UploadFile();
//                uf.setFileName(item.getOriginalFilename());
//                uf.setFileSize(item.getSize());
//                uf.setBytes(item.getBytes());
//                uploadFileService.saveFile(uf,CS.currentUser());
//
//                result.add(uf.getId(), uf.getFileName(), uf.getFileSize(), uf.getFileUrl());
//            }
//        } catch (Exception e) {
//            log.error("", e);
//        }
//        return initMap(new Object[]{"state","msg"}, new Object[]{!ObjectUtils.isEmpty(result), result.getFiles()});
//    }

    //Content-Type: multipart/form-data
//    @ResponseBody
//    @RequestMapping(value="postfile", method=RequestMethod.POST)
//    public Object saveFile(UploadFile t, @RequestParam(value="file", required=false) MultipartFile file) {
//        UploadFile fid = null;
//        InputStream is = null;
//        try {
//            if (null != file) {
//                if(ObjectUtils.isEmpty(t.getFileName())) t.setFileName(file.getOriginalFilename());
//                if(ObjectUtils.isEmpty(t.getFileSize())) t.setFileSize(file.getSize());
//                if(ObjectUtils.isEmpty(t.getSuffix())) t.setSuffix(FileUtils.getExtension(file.getOriginalFilename()));
//                is = file.getInputStream();
//                t.setStream(is);
//            }
//            fid = uploadFileService.saveFile(t,CS.currentUser());
//        } catch (Exception e) {
//            log.error(t.getId(), e);
//        } finally {
//            try {
//                if(null != is) is.close();
//            } catch (Exception e) {}
//        }
//        return initMap(new Object[]{"state","msg"}, new Object[]{
//                (null != fid) && !ObjectUtils.isEmpty(fid.getId()), StringUtil.trimToEmpty(fid.getId())});
//    }

//    @ResponseBody
//    @RequestMapping(value="postblock", method=RequestMethod.POST)
//    public Object saveBlock(@RequestParam(value="fid") String id,
//            @RequestParam(value="Range") String range, @RequestParam(value="file") MultipartFile file) {
//        UploadFile oid = null;
//        InputStream is = null;
//        try {
//          //Range:[first byte pos]-[last byte pos]
//            String[] arry = range.split("-");
//            long rangeFirst = MathUtils.toLong(arry[0], 0);
//            long rangeLast = MathUtils.toLong(arry[1], 0);
//            if(file.getSize() == (rangeLast - rangeFirst + 1)) {
//                UploadFile t = new UploadFile();
//                t.setId(id);
//                t.setRange(rangeFirst);
//                is = file.getInputStream();
//                t.setStream(is);
//                oid = uploadFileService.saveFile(t,CS.currentUser());
//            }
//        } catch (Exception e) {
//            log.error(id, e);
//        } finally {
//            try {
//                if(null != is) is.close();
//            } catch (Exception e) {}
//        }
//        return initMap(new Object[]{"state","msg"}, new Object[]{
//                (null != oid) && !ObjectUtils.isEmpty(oid.getId()), StringUtil.trimToEmpty(oid.getId())});
//    }

  //Content-Type:application/octet-stream
//    @ResponseBody
//    @RequestMapping(value="postbyte", method=RequestMethod.POST)
//    public Object save(@RequestHeader(value="fid", required=true) String id,
//            @RequestHeader(value="Range", required=true) String range, @RequestBody byte[] filebytes) {
//        UploadFile oid = null;
//        try {
//            //Range:[first byte pos]-[last byte pos]
//            String[] arry = range.split("-");
//            long rangeFirst = MathUtils.toLong(arry[0], 0);
//            long rangeLast = MathUtils.toLong(arry[1], 0);
//            if(null != filebytes && filebytes.length == (rangeLast - rangeFirst + 1)) {
//                UploadFile t = new UploadFile();
//                t.setId(id);
//                t.setRange(rangeFirst);
//                t.setBytes(filebytes);
//                oid = uploadFileService.saveFile(t,CS.currentUser());
//            }
//        } catch (Exception e) {
//            log.error("", e);
//        }
//        return initMap(new Object[]{"state","msg"}, new Object[]{
//                (null != oid) && !ObjectUtils.isEmpty(oid.getId()), StringUtil.trimToEmpty(oid.getId())});
//    }
}
