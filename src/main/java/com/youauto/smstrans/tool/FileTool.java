package com.youauto.smstrans.tool;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLDecoder;
import java.util.Properties;

@Component
public class FileTool {

    /**
     * 从配置文件.properties中取的参数值
     * @param name 参数名
     * @return 参数值
     * @throws Exception
     */
    public String getParamByName(String name,String fileName) throws Exception{
        String filePath = FileTool.class.getClassLoader().getResource(fileName).getPath();
        filePath = URLDecoder.decode(filePath,"utf-8");
        InputStream is = new FileInputStream(filePath);
        Properties pro = new Properties();
        String value = null;
        try {
            pro.load(is);
            value = pro.getProperty(name);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("读取配置文件失败!");
        }finally{
            if(null != is){
                is.close();
                is = null;
            }
        }
        return value;
    }
    public String getParamByNameDecode(String name,String fileName) throws Exception{
        InputStreamReader is = new InputStreamReader(FileTool.class.getClassLoader().getResourceAsStream(fileName), "GBK");
        Properties pro = new Properties();
        String value = null;
        try {
            pro.load(is);
            value = pro.getProperty(name);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("读取配置文件失败!");
        }finally{
            if(null != is){
                is.close();
                is = null;
            }
        }
        return value;
    }


    /**
     * 更新properties文件的键值对
     * @param name 键名
     * @param value 键值
     * @param fileName 属性文件名字
     * @throws Exception
     */
    public void updateProperties(String name, String value, String fileName) throws Exception{

        Properties pro = new Properties();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        String filePath = FileTool.class.getClassLoader().getResource(fileName).getPath();
        filePath = URLDecoder.decode(filePath,"utf-8");
        try{
            fis = new FileInputStream(filePath);
            pro.load(fis);
            pro.setProperty(name,value);
            fos = new FileOutputStream(filePath);
            pro.store(fos,"update "+name+"[value="+value+"]");
            fos.flush();
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("写入配置文件失败!");
        }finally {
            if(null != fos){
                fos.close();
            }
            if(null != fis){
                fis.close();
            }
        }
    }

    public void inputStreamToFile(InputStream is, File targetFile) throws Exception{
        FileOutputStream fileOutputStream = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bous = null;
        try{
            if(!targetFile.exists()){
                targetFile.createNewFile();
            }
            fileOutputStream  = new FileOutputStream(targetFile);
            bous = new BufferedOutputStream(fileOutputStream);
            bis = new BufferedInputStream(is);
            int count = -1;
            byte[] bytes = new byte[1024];
            while((count = bis.read(bytes,0,1024)) != -1){
                bous.write(bytes,0,count);
            }
            bous.flush();
            bytes = null;
        }catch(Exception e){
            e.printStackTrace();
            throw  new Exception(e.getMessage());
        }finally {
            if(null != fileOutputStream){
                fileOutputStream.close();
            }
            if(null != bous){
                bous.close();
            }
            if(null != bis){
                bis.close();
            }
        }

    }

    public void byteArrayToFile(byte[] data, File targetFile) throws Exception{
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bous = null;
        try{
            if(!targetFile.exists()){
                targetFile.createNewFile();
            }
            fileOutputStream  = new FileOutputStream(targetFile);
            bous = new BufferedOutputStream(fileOutputStream);
            bous.write(data);
            bous.flush();
        }catch(Exception e){
            e.printStackTrace();
            throw  new Exception(e.getMessage());

        }finally {
            if(null != fileOutputStream){
                fileOutputStream.close();
            }
            if(null != bous){
                bous.close();
            }
        }

    }

    public byte[] FileToByteArray(File srouceFile) throws Exception{
        FileInputStream fileInputStream = null;
        BufferedInputStream bis = null;
        byte[] content = null;
        try{
            if(!srouceFile.exists()){
                throw new Exception("文件不存在!");
            }
            fileInputStream = new FileInputStream(srouceFile);
            bis = new BufferedInputStream(fileInputStream);
            content = new byte[bis.available()];
            bis.read(content);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(bis != null){
                try{
                    bis.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            if(fileInputStream != null){
                try{
                    fileInputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return content;
    }
    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName
     *            要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public  boolean delete(String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                return false;
            } else {
                if (file.isFile())
                    return deleteFile(fileName);
                else
                    return deleteDirectory(fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public  boolean deleteFile(String fileName) {
        try {
            File file = new File(fileName);
            // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir
     *            要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public  boolean deleteDirectory(String dir) {
        try {
            // 如果dir不以文件分隔符结尾，自动添加文件分隔符
            if (!dir.endsWith(File.separator))
                dir = dir + File.separator;
            File dirFile = new File(dir);
            // 如果dir对应的文件不存在，或者不是一个目录，则退出
            if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
                return false;
            }
            boolean flag = true;
            // 删除文件夹中的所有文件包括子目录
            File[] files = dirFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 删除子文件
                if (files[i].isFile()) {
                    flag = deleteFile(files[i].getAbsolutePath());
                    if (!flag)
                        break;
                }
                // 删除子目录
                else if (files[i].isDirectory()) {
                    flag = deleteDirectory(files[i]
                            .getAbsolutePath());
                    if (!flag)
                        break;
                }
            }
            if (!flag) {
                return false;
            }
            // 删除当前目录
            if (dirFile.delete()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /*
        此方法只能创建一个文件
     */
    public boolean createDir(String path){
        try{
            File rootDirFile = new File(path);
            if(!rootDirFile.exists()){
                return rootDirFile.mkdir();
            }else{
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /*
    创建多层
     */
    public boolean createDirs(String path){
        try{
            File file = new File(path);
             //如果路径不存在，新建
            if(!file.exists()&&!file.isDirectory()) {
                return file.mkdirs();
            }else{
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
