package blog.logic;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TranslitTest {
    @Test
    public void testToTranslit() {
        String testString = "В конце прошлого года правительство объявило, что планирует снизить таможенный лимит с €1000 до €150. Таможенники подхватили эту инициативу и высказали желание ограничить вес беспошлинного ввоза до";
        String result = "V konce proshlogo goda pravitelstvo obyavilo, chto planiruet snizit tamozhennyi limit s 1000 do 150. Tamozhenniki podhvatili etu iniciativu i vyskazali zhelanie ogranichit ves besposhlinnogo vvoza do";
        Assert.assertEquals(Translit.toTranslit(testString), result);
    }
}
