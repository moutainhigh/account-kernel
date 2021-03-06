package OLink.bpm.util.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ExcelCellStyleUtils {
	// 标题样式
	public HSSFCellStyle titleStyle;

	// 时间样式
	public HSSFCellStyle dataStyle;

	// 单元格样式
	public HSSFCellStyle nameStyle;

	// 超链接样式
	public HSSFCellStyle linkStyle;

	public HSSFFont font;

	public ExcelCellStyleUtils(ExcelWorkBook work) {
		titleStyle = linkStyle(work.getWorkbook());
		dataStyle = dataStyle(work.getWorkbook());
		nameStyle = nameStyle(work.getWorkbook());
		linkStyle = linkStyle(work.getWorkbook());
	}

	/**
	 * 超链接样式
	 * 
	 * @return HSSFCellStyle
	 */
	private HSSFCellStyle linkStyle(HSSFWorkbook work) {
		HSSFCellStyle linkStyle = work.createCellStyle();
		linkStyle.setBorderBottom((short) 1);
		linkStyle.setBorderLeft((short) 1);
		linkStyle.setBorderRight((short) 1);
		linkStyle.setBorderTop((short) 1);
		linkStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		linkStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		HSSFFont font = work.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setUnderline((byte) 1);
		font.setColor(HSSFColor.BLUE.index);
		linkStyle.setFont(font);
		return linkStyle;
	}

	/**
	 * s 单元格样式
	 * 
	 * @return HSSFCellStyle
	 */
	private HSSFCellStyle nameStyle(HSSFWorkbook work) {
		HSSFCellStyle nameStyle = work.createCellStyle();
		nameStyle.setBorderBottom((short) 1);
		nameStyle.setBorderLeft((short) 1);
		nameStyle.setBorderRight((short) 1);
		nameStyle.setBorderTop((short) 1);
		nameStyle.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
		nameStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return nameStyle;
	}

	/**
	 * 时间样式
	 * 
	 * @return HSSFCellStyle
	 */
	private HSSFCellStyle dataStyle(HSSFWorkbook work) {
		HSSFCellStyle dataStyle = work.createCellStyle();
		dataStyle.setBorderBottom((short) 1);
		dataStyle.setBorderLeft((short) 1);
		dataStyle.setBorderRight((short) 1);
		dataStyle.setBorderTop((short) 1);
		dataStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		dataStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return dataStyle;
	}

	/**
	 * 标题样式
	 * 
	 * @return HSSFCellStyle
	 */
	/*
	private static HSSFCellStyle titleStyle(HSSFWorkbook work) {
		HSSFCellStyle titleStyle = work.createCellStyle();
		font = work.createFont();
		font.setItalic(true);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.BLUE.index);
		titleStyle.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
		titleStyle.setBorderLeft((short) 1);
		titleStyle.setBorderRight((short) 1);
		titleStyle.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
		titleStyle.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return titleStyle;
	}
	*/
}
