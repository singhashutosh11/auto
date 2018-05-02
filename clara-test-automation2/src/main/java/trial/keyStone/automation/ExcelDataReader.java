package trial.keyStone.automation;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

public class ExcelDataReader {

	private String ExcelPath;
	private String pathToExcel;
	private String sheetName;

	public ExcelDataReader(String excelPath) {
		try {
			ExcelPath = excelPath;
			String[] pathElements = excelPath.split(Pattern.quote(";"));
			this.pathToExcel = pathElements[0].trim();
			this.sheetName = pathElements[1].trim();
			if (sheetName.isEmpty()) {
				throw new RuntimeException("sheet name is empty.");
			}
		} catch (Exception e) {
			throw new RuntimeException("Invalid excel path '" + excelPath + "'", e);
		}
	}

	@DataProvider
	public ExcelData[] readExcelData(ITestContext context) {
		try {
			FileInputStream file = new FileInputStream(pathToExcel);
			Workbook wb = new HSSFWorkbook(file);
			Sheet ws = wb.getSheet(sheetName);
			Set<ExcelData> excelData = new LinkedHashSet<ExcelData>();
			ArrayList<HashMap<String, String>> aL = new ArrayList<HashMap<String, String>>();
			String Key = null;
			int rowCount = ws.getLastRowNum();
			for (int i = 0; i <= rowCount; i++) {
				Row row = ws.getRow(i);
				if (row == null) {
					continue;
				}
				String tempKey = row.getCell(0).getStringCellValue();
				if (tempKey != null) {
					Key = tempKey;
				}
				for (int j = 1; j < row.getLastCellNum(); j++) {
					String cellValue = getValue((HSSFCell) row.getCell(j));
					// String cellValue = row.getCell(j).getStringCellValue();
					if (aL.size() < j) {
						aL.add(new HashMap<String, String>());
					}
					if (cellValue != null) {
						cellValue = cellValue.trim();
					}
					if (!aL.get(j - 1).containsKey(Key)) {
						aL.get(j - 1).put(Key, null);
					}
					aL.get(j - 1).put(Key, cellValue);
				}
			}
			for (Map<String, String> map : aL) {
				if (map.isEmpty()) {
					// Discard this data.
					continue;
				}
				ExcelData data = new ExcelData(map);
				if (data.getId() == null || data.getId().trim().isEmpty()) {
					continue;
				}
				excelData.add(data);
			}
			return excelData.toArray(new ExcelData[] {});
		} catch (Exception e) {
			throw new RuntimeException("Could not read data from ", e);
		}
	}

	private static String getValue(HSSFCell cell) {
		try {
			if (cell == null) {
				return null;
			} //check if cell value is boolean
			else if (HSSFCell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
				return Boolean.toString(cell.getBooleanCellValue());
			}
			else {//else convert both string and non-string values to string
				return cell.getStringCellValue();
			}
		} catch (NumberFormatException ex) {
			return null;//NumberToTextConverter.toText(cell.getNumericCellValue());
		}
		
	}

}
