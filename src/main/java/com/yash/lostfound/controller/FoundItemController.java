package com.yash.lostfound.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.yash.lostfound.model.FoundItem;
import com.yash.lostfound.service.FoundItemService;

import jakarta.servlet.http.HttpSession;

import java.io.File;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class FoundItemController {

    @Autowired
    private FoundItemService service;

    @GetMapping("/report")
    public String reportForm(Model model) {
        model.addAttribute("item", new FoundItem());
        return "report-found";
    }

    @PostMapping("/save")
    public String saveItem(@ModelAttribute FoundItem item,
                           @RequestParam("imageFile") MultipartFile file) {

        try {

            if (!file.isEmpty()) {

                // Absolute path to project root
                String uploadDir = System.getProperty("user.dir") + "/uploads/";

                File uploadPath = new File(uploadDir);

                if (!uploadPath.exists()) {
                    uploadPath.mkdirs();
                }

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                file.transferTo(new File(uploadDir + fileName));

                item.setImagePath("/uploads/" + fileName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        service.saveItem(item);
        return "redirect:/";
    }



    @GetMapping("/view")
    public String viewItems(Model model) {
        model.addAttribute("items", service.getAllItems());
        return "view-items";
    }

    @GetMapping("/admin")
    public String adminPage(Model model, HttpSession session) {

        if(session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        model.addAttribute("items", service.getAllItems());
        return "admin-dashboard";
    }


    @GetMapping("/claim/{id}")
    public String markClaimed(@PathVariable Long id, HttpSession session) {

        if(session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        service.markClaimed(id);
        return "redirect:/admin";
    }

}
