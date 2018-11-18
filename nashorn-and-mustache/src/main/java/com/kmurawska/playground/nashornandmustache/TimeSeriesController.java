package com.kmurawska.playground.nashornandmustache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.script.ScriptException;

@Controller
public class TimeSeriesController {

    private final PageContentCreator contentCreator;

    @Autowired
    public TimeSeriesController(PageContentCreator contentCreator) {
        this.contentCreator = contentCreator;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String allCharacters() {
        try {
            return contentCreator.create(new DataLoader().load());
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
