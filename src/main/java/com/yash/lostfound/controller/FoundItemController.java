package com.yash.lostfound.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.yash.lostfound.model.FoundItem;
import com.yash.lostfound.service.FoundItemService;

import jakarta.servlet.http.HttpSession;
import java.util.Base64;

@Controller
public class FoundItemController {

    @Autowired
    private FoundItemService service;

    // ── GET /report ──────────────────────────────────────────────
    @GetMapping("/report")
    public String reportForm(Model model) {
        model.addAttribute("item", new FoundItem());
        return "report-found";
    }

    // ── POST /save ───────────────────────────────────────────────
    @PostMapping("/save")
    public String saveItem(
            @ModelAttribute FoundItem item,
            @RequestParam("imageFile") MultipartFile file) {

        try {
            if (!file.isEmpty()) {
                // Convert image to Base64 — no filesystem needed, works on Railway ✅
                byte[] bytes = file.getBytes();
                String base64 = Base64.getEncoder().encodeToString(bytes);
                String mimeType = file.getContentType(); // e.g. "image/jpeg"
                item.setImageData("data:" + mimeType + ";base64," + base64);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Default status to Available when first reported
        if (item.getStatus() == null || item.getStatus().isBlank()) {
            item.setStatus("Available");
        }

        service.saveItem(item);
        return "redirect:/";
    }

    // ── GET /view ────────────────────────────────────────────────
    @GetMapping("/view")
    public String viewItems(Model model) {
        model.addAttribute("items", service.getAllItems());
        return "view-items";
    }

    // ── GET /admin ───────────────────────────────────────────────
    @GetMapping("/admin")
    public String adminPage(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }
        model.addAttribute("items", service.getAllItems());
        return "admin-dashboard";
    }

    // ── GET /claim/{id} ──────────────────────────────────────────
    @GetMapping("/claim/{id}")
    public String markClaimed(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }
        service.markClaimed(id);
        return "redirect:/admin";
    }
}
