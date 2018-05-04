package OLink.bpm.core.networkdisk.action;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import OLink.bpm.core.networkdisk.ejb.NetDiskFile;
import OLink.bpm.core.networkdisk.ejb.NetDiskFileProcess;
import OLink.bpm.util.ProcessFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class FileManagerUploadServlet
 */
public class NetworkDiskAdminUploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7722958763580172908L;

	// 通过doget请求处理
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	// 通过dopost请求处理
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	// 真正处理请求的方法
	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 将上传文件信息保存包数据库中
		NetDiskFileProcess netDiskFileProcess = null;// 文件信息
		try {
			netDiskFileProcess = (NetDiskFileProcess) ProcessFactory
					.createProcess(NetDiskFileProcess.class);
		} catch (Exception e) {
			e.printStackTrace();

		}

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		String realPath = request.getParameter("realPath");
		String webPath = realPath.substring(realPath.indexOf("networkdisk")-1);
		webPath = webPath.replaceAll("\\\\", "/");
		String userid = request.getParameter("userid");
		File f1 = new File(realPath);
		if (!f1.exists()) {
			if (!f1.mkdirs())
				throw new IOException("create directory '"
						+ f1.getAbsoluteFile() + "' failed!");
		}
		DiskFileItemFactory fac = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		List fileList = null;
		try {
			fileList = upload.parseRequest(request);
		} catch (FileUploadException ex) {
			ex.printStackTrace();
			return;
		}
		Iterator<FileItem> it = fileList.iterator();
		String name = "";// 文件名+文件扩展名
		String fileName = "";// 文件名+文件扩展名
		String extName = "";// 文件扩展名
		long size = 0;// 图片大小
		while (it.hasNext()) {
			FileItem item = it.next();
			if (item != null && !item.isFormField()) {
				name = item.getName();// 获得文件名和扩展名
				if (name != null) {
					fileName = name.substring(0, name.lastIndexOf("."));// 获得文件名
					// 扩展名格式：
					if (name.lastIndexOf(".") >= 0) {
						extName = name.substring(name.lastIndexOf("."));
					}
				}
				size = item.getSize();// 获得图片大小
				@SuppressWarnings("unused")
				String type = item.getContentType();

				if (name == null || name.trim().equals("")) {
					continue;
				}
				// 通过递归实现已存在文件的重命名
				NetDiskFile netDiskFile = new NetDiskFile();
				netDiskFile.setFolderPath(realPath.trim());
				netDiskFile.setName(fileName + extName);
				reName(netDiskFile,0,fileName,extName);

				try {
					File saveFile = new File(realPath +"\\"+netDiskFile.getName());
					item.write(saveFile);
					netDiskFile.setType(extName);
					netDiskFile.setSize(size);
					if(userid!=null){
						netDiskFile.setUserid(userid);
					}
					netDiskFile.setModifyTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					netDiskFile.setFolderWebPath(webPath);
					//netDiskFile.setPemissionid(userid);
					netDiskFileProcess.doCreate(netDiskFile);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
		response.getWriter().print("aa");
	}

	//递归文件名称
	protected void reName(NetDiskFile netDiskFile,int i,String fileName,String extName){
		File file = new File(netDiskFile.getFolderPath()+"\\"+netDiskFile.getName());
		if(file.exists()){
			i++;
			netDiskFile.setName(fileName+i+extName);
			reName(netDiskFile,i,fileName,extName);
		}
	}
}
