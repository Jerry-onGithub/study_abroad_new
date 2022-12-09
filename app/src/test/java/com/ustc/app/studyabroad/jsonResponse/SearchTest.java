package com.ustc.app.studyabroad.jsonResponse;

import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.models.University;

import junit.framework.TestCase;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchTest extends TestCase {

    private final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void testGetResponse() {
        String newText = "To";
        String query = "uni_name";

        LOGGER.log(Level.INFO, "My first Log Message");
        System.out.println("SIZE is: ");

        Search search = new Search();
        search.getResponse(newText, query, "", new CustomCallback() {
            @Override
            public void onSuccess(List<University> universityList) {
                LOGGER.log(Level.INFO, "SIZE: " + String.valueOf(universityList.size()));
                //System.out.println("SIZE is: " + universityList.size() + " " + universityList.get(0));
            }
            @Override
            public void onFailure() {
                LOGGER.log(Level.INFO,"Failed");
            }
        });
    }
}