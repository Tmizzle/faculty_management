package com.asss.management.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/finances")
@Data
@Tag(name = "Finances API", description = "API for managing finances")
@CrossOrigin(origins = "*")
public class FinancesController {
}
