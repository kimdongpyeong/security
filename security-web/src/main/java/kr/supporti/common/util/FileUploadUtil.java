package kr.supporti.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadUtil {

    public static final int BUFF_SIZE = 2048;

    public static File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        File convFile = new File(multipart.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipart.getBytes());
        fos.close();
        return convFile;
    }

    public static String makeSystemFileNm(String fileNm) throws Exception {
        String strSystemFileNm = "";
        SimpleDateFormat sdfCurrent = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREA);
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        // strSystemFileNm = fileNm.substring(0, fileNm.lastIndexOf("."))
        strSystemFileNm = UUID.randomUUID().toString().replaceAll("-", "") + "_" + sdfCurrent.format(ts.getTime());

        return strSystemFileNm;
    }

    public static String makeSystemPathYYYYMM(String baseFilePath) throws Exception {
        String strSystemFilePath = "";
        Calendar cal = Calendar.getInstance();
        strSystemFilePath = baseFilePath + File.separator + cal.get(Calendar.YEAR) + File.separator
                + (cal.get(Calendar.MONTH) + 1);
        return strSystemFilePath;
    }

    public static Boolean writeFile(MultipartFile file, String newName, String stordFilePath, String ext) {

        int bytesRead = 0;
        File cFile = null;
        InputStream stream = null;
        OutputStream bos = null;
        byte[] buffer = null;

        try {
            if (stordFilePath.equalsIgnoreCase("..") || stordFilePath.equalsIgnoreCase("./"))
                return false;

            stream = file.getInputStream();
            cFile = new File(stordFilePath);

            if (!cFile.isDirectory()) {
                boolean _flag = cFile.mkdirs();
                if (!_flag) {
                    throw new IOException("Directory creation Failed ");
                }
            }

            bos = new FileOutputStream(stordFilePath + File.separator + newName + "." + ext);

            buffer = new byte[BUFF_SIZE];

            while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        } catch (FileNotFoundException fnfe) {
            return false;
        } catch (IOException ioe) {
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    return false;
                } catch (Exception ignore) {
                    return false;
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    return false;
                } catch (Exception ignore) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void writeToFile(String path, String filename, byte[] pData) {
        if (pData == null) {
            return;
        }
        FileOutputStream fileOutputStream = null;
        try {
            Files.createDirectories(Paths.get(path));
            File outFile = new File(path + filename);
            fileOutputStream = new FileOutputStream(outFile);
            fileOutputStream.write(pData);
            fileOutputStream.close();
        } catch (IOException e) {
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
            } catch (NullPointerException e2) {
            }
        }
    }
}
