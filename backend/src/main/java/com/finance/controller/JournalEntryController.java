package com.finance.controller;

import com.finance.common.Result;
import com.finance.entity.JournalEntry;
import com.finance.service.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/journal-entry")
public class JournalEntryController {
    
    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("/list")
    public Result<List<JournalEntry>> list() {
        return Result.success(journalEntryService.findAll());
    }

    @GetMapping("/{id}")
    public Result<JournalEntry> getById(@PathVariable Long id) {
        JournalEntry entry = journalEntryService.findById(id);
        if (entry != null) {
            return Result.success(entry);
        }
        return Result.error("会计分录不存在");
    }

    @GetMapping("/status/{status}")
    public Result<List<JournalEntry>> getByStatus(@PathVariable String status) {
        return Result.success(journalEntryService.findByStatus(status));
    }

    @GetMapping("/date-range")
    public Result<List<JournalEntry>> getByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return Result.success(journalEntryService.findByDateRange(startDate, endDate));
    }

    @PostMapping("/save")
    public Result<JournalEntry> save(@RequestBody JournalEntry journalEntry) {
        try {
            JournalEntry saved = journalEntryService.save(journalEntry);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    @PostMapping("/post/{id}")
    public Result<JournalEntry> post(@PathVariable Long id) {
        try {
            JournalEntry posted = journalEntryService.post(id);
            if (posted != null) {
                return Result.success("过账成功", posted);
            }
            return Result.error("过账失败：凭证不存在");
        } catch (Exception e) {
            return Result.error("过账失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            journalEntryService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}










