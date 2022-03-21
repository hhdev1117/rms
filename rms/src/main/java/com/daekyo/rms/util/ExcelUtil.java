package com.daekyo.rms.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 공통 엑셀 다운로드 유틸 
 * 1. ExcelUtil getInstance로 객체생성 
 * 2. request, response SET 
 * 3. instance.runToExcel() 호출
 * 
 * xssf -> 읽기, 쓰기 가능 -> 메모리 Flush 기능이 없어서 대용량 다운불가능
 * sxssf -> 쓰기만 가능 -> 메모리 Flush 기능추가로 대용량도 문제없음
 * 
 * 템플릿 읽기 -> xssf(읽기) -> sxssf로 변환(xssf->sxssf 가능)
 * -> sxssf로 엑셀작성 -> xssf로 변환(sxssf -> xssf 불가능함. sxssf를 inputstream에 담아서 xssf를 inputstream으로 생성)
 * -> xssf를 암호화(sxssf는 쓰기전용이므로 읽어서 암호를 입히는것은 불가능)
 * -> xssf를 다운로드
 */
public class ExcelUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
	
	private static ExcelUtil excelUtil = new ExcelUtil();
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String filename = "temp";
	private String title = "";
	private int titleindex = 0;

	public static ExcelUtil getInstance() {
		return excelUtil;
	}

	private ExcelUtil() {
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setTitleindex(int titleindex) {
		this.titleindex = titleindex;
	}

	/**
	 * 타이틀을 설정한다.
	 * @param sheet : 타이틀 설정할 시트
	 * @param index : 타이틀이 있는 row index
	 * @return
	 */
	private XSSFCell createTitleRow(XSSFSheet sheet, int index) {
		XSSFRow row = sheet.getRow(index);
		XSSFCell cell = row.getCell(0);
		
		cell.setCellValue(title);
		return cell;
	}

	/**
	 * 템플릿을 읽어서 엑셀 객체를 리턴한다. 서비스에서 엑셀을 구현하고 runToExcel()로 다운을 한다.
	 * 
	 * @return
	 */
	public XSSFWorkbook getXssfWorkbook(String templateFileName) {
		XSSFWorkbook xssfWorkbook = null;
		InputStream is = null;
		FileInputStream fis = null;
		try {
			// 템플릿파일 가져오기
			String templatePath = request.getSession().getServletContext().getRealPath("/WEB-INF/template/");
			fis = new FileInputStream(templatePath + templateFileName);
			is = new BufferedInputStream(fis);
			xssfWorkbook = new XSSFWorkbook(is);

		} 
		catch (NullPointerException e) {
			logger.debug("[NullPointerException] getXssfWorkbook -" + e );
		}
		catch (IOException e) {			
			logger.debug("[IOException] getXssfWorkbook -" + e );	
		}	
		
		catch (Exception e) {
			logger.debug("[Exception] getXssfWorkbook -" + e );	
		}finally {
			
			if(fis != null) {
				
				try {
					fis.close();
					
				} catch (IOException e2) {
					logger.debug("[IOException] getXssfWorkbook -" + e2 );	
					
				}
				
				catch (Exception e2) {
					logger.debug("[Exception] getXssfWorkbook -" + e2 );	
					
				}
				
				
			}
			
		}

		return xssfWorkbook;
	}
	
	public SXSSFWorkbook getSxssfWorkbook(String templateFileName) {
		SXSSFWorkbook sxssfWorkbook = null;
		try {
			// 템플릿파일 가져오기
			String templatePath = request.getSession().getServletContext().getRealPath("/WEB-INF/template/");
			InputStream is = new BufferedInputStream(new FileInputStream(templatePath  + templateFileName));
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);

			// 타이틀이 null이 아니면
			// 타이틀 적용해줍니다.
			if(title != null) {
				createTitleRow(xssfWorkbook.getSheetAt(0), titleindex);
			}
			// sxssfWorkbook 생성.
			// 10개 작성때마다 flush를 한다.
			// sxssf 방식은 쓰기전용이라서 템플릿을 읽지못하기 때문에
			// xssf로 템플릿을 읽은다음에 sxssf로 변환한다.
			// 후에 암호화를 위해 inputstream에 담아서 다시 xssf에 쓴다.
			sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook, 10);
			is.close();
			
		} 
		catch (NullPointerException e) {
			logger.debug("[NullPointerException] getSxssfWorkbook -" + e );
		}
		catch (IOException e) {			
			logger.debug("[IOException] getSxssfWorkbook -" + e );	
		}	
		
		catch (Exception e) {
			logger.debug("[Exception] getSxssfWorkbook -" + e );	
		}
		
		return sxssfWorkbook;
	}

	/**
	 * 엑셀 다운
	 */
	public void runToExcel(SXSSFWorkbook workbook, String excelpass) throws Exception {
		try {
			// 엑셀다운 response 설정
			response.setContentType("Application/Msexcel");
			response.setHeader("Content-Disposition",
					"ATTachment; Filename=" + URLEncoder.encode(filename, "UTF-8") + ".xlsx");
			
			// sxssf는 읽기가 불가능하므로 워크북을 읽어서 암호를 입히는것이 안된다.
			// 따라서 inputstream에 저장후 다시 암호를 입힐 xssf를 생성한다.
			ByteArrayOutputStream tempOs = new ByteArrayOutputStream();
			workbook.write(tempOs);
			InputStream in = new ByteArrayInputStream(tempOs.toByteArray());
			
			// 파일을 Output하기위한 객체
			POIFSFileSystem fs = new POIFSFileSystem();

			// 파일 암호화
			OPCPackage opc = OPCPackage.open(in);
			OutputStream os = getEncryptOutputStream(fs, excelpass);
			opc.save(os);
			opc.close();

			response.setHeader("Set-Cookie", "fileDownload=true; path=/");

			OutputStream resOS = response.getOutputStream();
			fs.writeFilesystem(resOS);
			
			// 썻던 stream들 다 닫아줍니다.
			tempOs.close();
			in.close();
			resOS.close();
			fs.close();
			workbook.close();
			
		} 
		catch (NullPointerException e) {
			logger.debug("[NullPointerException] runToExcel -" + e );
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
		}
		catch (IOException e) {			
			logger.debug("[IOException] runToExcel -" + e );	
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
		}	
		catch (Exception e) {
			logger.debug("[Exception] runToExcel -" + e );	
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
		}
	}

	/**
	 * 파일 암호화
	 * 
	 * @param fileSystem
	 * @return OutputStream
	 */
	private OutputStream getEncryptOutputStream(POIFSFileSystem fileSystem, String excelpass) {
		try {
			Encryptor enc = new EncryptionInfo(EncryptionMode.agile).getEncryptor();
			enc.confirmPassword(excelpass);

			return enc.getDataStream(fileSystem);
		} 
		catch (NullPointerException e) {
			logger.debug("[NullPointerException] getEncryptOutputStream -" + e );
		}
		catch (IOException e) {			
			logger.debug("[IOException] getEncryptOutputStream -" + e );			
		}	
		catch (Exception e) {
			logger.debug("[Exception] getEncryptOutputStream -" + e );		
		}
		return null;
	}
}
