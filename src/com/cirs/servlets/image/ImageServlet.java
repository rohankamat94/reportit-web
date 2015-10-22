package com.cirs.servlets.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cirs.core.CIRSConstants;

@SuppressWarnings("serial")
public class ImageServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("here in servlet " + req.getPathInfo());
		final String imagePath = CIRSConstants.IMAGE_ROOT + req.getPathInfo().replaceAll("/", "\\\\");
		System.out.println(imagePath);
		Path path = Paths.get(imagePath);

		if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
			setResponseOutput(resp, path);
			return;
		} else if (req.getPathInfo().contains("users")) {
			path = CIRSConstants.getUserImageDir().resolve("default.png");
			setResponseOutput(resp, path);
			return;
		}
		resp.sendError(HttpServletResponse.SC_NOT_FOUND);

	}

	private void setResponseOutput(HttpServletResponse resp, Path path) throws IOException {
		resp.reset();
		resp.setContentLengthLong(Files.size(path));
		resp.setContentType(Files.probeContentType(path));
		Files.copy(path, resp.getOutputStream());
	}
}
