package blog.logic;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TranslitTest {
    @Test
    public void testToTranslit() {
        String testString = "В конце прошлого года правительство объявило, что планирует снизить таможенный лимит с €1000 до €150. Таможенники подхватили эту инициативу и высказали желание ограничить вес беспошлинного ввоза до";
        String result = "V konce proshlogo goda pravitel_stvo obyavilo_ chto planiruet snizit_ tamozhennyi limit s _1000 do _150_ Tamozhenniki podhvatili etu iniciativu i vyskazali zhelanie ogranichit_ ves besposhlinnogo vvoza do";
        Assert.assertEquals(Translit.toTranslit(testString), result);
    }
}
