package com.example.servlet;

import com.example.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

@WebServlet("/")
public class MainServlet extends HttpServlet {
    private static final String BASE_DIR = System.getProperty("user.home") + File.separator + "filemanager" + File.separator;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");
        String userHome = BASE_DIR + user.getLogin();
        File userHomeDir = new File(userHome);
        if (!userHomeDir.exists()) {
            userHomeDir.mkdirs();
        }
        String path;
        String pathParam = req.getParameter("path");
        if(pathParam != null && !pathParam.isEmpty())
            path = URLDecoder.decode(pathParam, StandardCharsets.UTF_8);
        else
            path = userHome;
        File thisObject = new File(path);
        if (!thisObject.getCanonicalPath().startsWith(new File(userHome).getCanonicalPath())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещён");
            return;
        }
        if(!thisObject.exists()){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Путь не найден: " + path);
            return;
        }
        if(thisObject.isFile()){
            String mimeType = Files.probeContentType(thisObject.toPath());
            if (mimeType == null)
                mimeType = "application/octet-stream";
            resp.setContentType(mimeType);
            resp.setHeader("Content-Disposition",
                    "attachment; filename=\"" + thisObject.getName() + "\"");
            Files.copy(thisObject.toPath(), resp.getOutputStream());
            return;
        }
        File[] contentList = thisObject.listFiles();
        if (contentList != null)
            Arrays.sort(contentList, Comparator.comparing(File::getName));
        req.setAttribute("content", contentList);
        req.setAttribute("parentPath", thisObject.getParent());
        req.setAttribute("currentPath", thisObject.getAbsolutePath());
        req.setAttribute("generatedTime", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()));
        req.getRequestDispatcher("mypage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doPost(req, resp);
    }
}