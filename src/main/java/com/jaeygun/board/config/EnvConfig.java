package com.jaeygun.board.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
public class EnvConfig implements EnvironmentAware{

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getStringValue(String key) {

        return environment.getProperty(key);
    }

    public int getIntValue(String key) {

        return Integer.parseInt(environment.getProperty(key));
    }

    public List<Integer> getIntListValue(String key) {

        String[] values = environment.getProperty(key).split(",");
        List<Integer> dateDiffList = new ArrayList<Integer>();
        for (int i=0; i < values.length; i++) {
            dateDiffList.add(Integer.parseInt(values[i].trim()));
        }
        return dateDiffList;
    }

    public boolean getBooleanValue(String key) {

        return Boolean.parseBoolean(environment.getProperty(key));
    }

}
