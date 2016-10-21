package com.thingword.alphonso;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;

import com.thingword.alphonso.bean.ReturnMessage;
import com.thingword.alphonso.bean.db.Product;
import com.thingword.alphonso.service.UploadService;
import com.thingword.alphonso.service.impl.ProductServiceImpl;
import com.thingword.alphonso.service.impl.UploadServiceImpl;

@Path("/upload")
public class UpLoadResource {

    @Autowired
    private ProductServiceImpl productServiceImpl;
    @Autowired
    private UploadServiceImpl uploadServiceImpl;

	@POST
	@Path("/getPro")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> getPro() {
		return productServiceImpl.ListAllProduct();
	}
	
	@POST
	@Path("/uploadProductCraftResource")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public ReturnMessage uploadProductCraftResource(@FormDataParam("filepath") InputStream uploadedInputStream,
			@FormDataParam("filepath") FormDataContentDisposition fileDetail) {
		return uploadServiceImpl.uploadProductCraftResource(fileDetail.getFileName(), uploadedInputStream);

	}
	


}