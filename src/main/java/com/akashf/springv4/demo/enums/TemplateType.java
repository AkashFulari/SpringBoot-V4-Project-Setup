package com.akashf.springv4.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TemplateType {
    IMPORT_USERS("Import Users", "import-users");

    private String name, fileName;
}
