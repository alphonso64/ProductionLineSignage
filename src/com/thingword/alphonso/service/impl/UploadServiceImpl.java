package com.thingword.alphonso.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType;
import com.mysql.jdbc.StreamingNotifiable;
import com.thingword.alphonso.bean.MESSAGE;
import com.thingword.alphonso.bean.ReturnMessage;
import com.thingword.alphonso.bean.db.Product;
import com.thingword.alphonso.dao.impl.ProductDaoImpl;
import com.thingword.alphonso.service.UploadService;
import com.thingword.alphonso.util.FileUtil;
import com.thingword.alphonso.util.ZipUtil;

public class UploadServiceImpl implements UploadService {

	@Autowired
	private ProductDaoImpl productDaoImpl;

	private static final String prePath = "D:\\upload\\";

	public ReturnMessage uploadProductCraftResource(String name, InputStream inputStream) {
		ReturnMessage returnMessage = new ReturnMessage();
		returnMessage.setReturn_code(MESSAGE.RETURN_FAIL);
		returnMessage.setReturn_msg(MESSAGE.UPLOAD_FIAL);
		String parent = parseProductCraftFileName(name);
		if (parent == null) {
			returnMessage.setReturn_msg(MESSAGE.UPLOAD_FAIL_FORMAT);
			return returnMessage;
		}
		Product product = new Product();
		product.setInvcode(parent);
		product.setPath(prePath + parent);
		if (!ZipUtil.unzip(inputStream, prePath + parent)) {
			returnMessage.setReturn_msg(MESSAGE.UPLOAD_FAIL_UNZIP);
			return returnMessage;
		}
		parseXlsFile(prePath + parent);
		productDaoImpl.updateProduct(product);
		returnMessage.setReturn_code(MESSAGE.RETURN_SUCCESS);
		returnMessage.setReturn_msg(MESSAGE.UPLOAD_SUCCESS);
		return returnMessage;
	};

	private void parseXlsFile(String parent) {
		List<File> ls = FileUtil.getXlsFileList(parent);
		for (File file : ls) {
			try {
				InputStream inp = new FileInputStream(file);
				HSSFWorkbook wb = new HSSFWorkbook(inp);
				int len = wb.getNumberOfSheets();
				wb.close();
				inp.close();
				for (int i = 0; i < len; i++) {
					inp = new FileInputStream(file);
					wb = new HSSFWorkbook(inp);
					String name = wb.getSheetAt(i).getSheetName();
					int j;
					for(j=0;j<i;j++){
						wb.removeSheetAt(0);
					}
					for(;j<len-1;j++){
						wb.removeSheetAt(1);
					}
					FileOutputStream output = new FileOutputStream(parent+"\\"+name+".xls");
					System.out.println("save:"+parent+"\\"+name);
					wb.write(output);
					wb.close();
					output.close();
					inp.close();
				}
			} catch (Exception e) {
				System.out.println("Exception:"+e.getMessage());
			}
		}
	}

	public void process(HSSFSheet sheet) throws IOException {
		HSSFWorkbook myWorkBook = new HSSFWorkbook();
		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFSheet mySheet = null;
		HSSFRow myRow = null;
		HSSFCell myCell = null;
		int fCell = 0;
		int lCell = 0;
		int fRow = 0;
		int lRow = 0;

		if (sheet != null) {
			mySheet = myWorkBook.createSheet(sheet.getSheetName());
			fRow = sheet.getFirstRowNum();
			lRow = sheet.getLastRowNum();
			for (int iRow = fRow; iRow <= lRow; iRow++) {
				row = sheet.getRow(iRow);
				myRow = mySheet.createRow(iRow);
				if (row != null) {
					fCell = row.getFirstCellNum();
					lCell = row.getLastCellNum();
					for (int iCell = fCell; iCell < lCell; iCell++) {
						cell = row.getCell(iCell);
						myCell = myRow.createCell(iCell);
						if (cell != null) {
							myCell.setCellType(cell.getCellType());
							switch (cell.getCellType()) {
							case HSSFCell.CELL_TYPE_BLANK:
								myCell.setCellValue("");
								break;

							case HSSFCell.CELL_TYPE_BOOLEAN:
								myCell.setCellValue(cell.getBooleanCellValue());
								break;

							case HSSFCell.CELL_TYPE_ERROR:
								myCell.setCellErrorValue(cell.getErrorCellValue());
								break;

							case HSSFCell.CELL_TYPE_FORMULA:
								myCell.setCellFormula(cell.getCellFormula());
								break;

							case HSSFCell.CELL_TYPE_NUMERIC:
								myCell.setCellValue(cell.getNumericCellValue());
								break;

							case HSSFCell.CELL_TYPE_STRING:
								myCell.setCellValue(cell.getStringCellValue());
								break;
							default:
								myCell.setCellFormula(cell.getCellFormula());
							}
						}
					}
				}
			}
		}

		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("D:\\workbook.xls", true));
		myWorkBook.write(bos);
		myWorkBook.close();
		bos.close();
	}

	private String parseProductCraftFileName(String name) {
		try {
			String pname = new String(name.getBytes("ISO8859-1"), "GBK");
			String regex = "\\d+\\.zip";
			Pattern p1 = Pattern.compile(regex);

			if (p1.matcher(pname).matches()) {
				return pname.substring(0, pname.length() - 4);
			}
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

}
