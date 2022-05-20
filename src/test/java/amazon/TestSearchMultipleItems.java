package amazon;

import base.CommonAPI;
import org.testng.annotations.Test;
import pages.BasePage;
import utility.DataReader;
import utility.Utility;

import java.util.List;

public class TestSearchMultipleItems extends CommonAPI {

    String path = Utility.root+"src/data/Books.xlsx";
    String sheet = "Sheet1";
    String header = "Books";

    DataReader dr = new DataReader();

    @Test
    public void searchMultipleItems(){
        List<String> list = dr.getEntireColumnForGivenHeader(path, sheet, header);

        for (String item: list){
            BasePage bp = new BasePage(getDriver());
            bp.searchElementAndEnterAndClearField(item);
        }

    }
}
