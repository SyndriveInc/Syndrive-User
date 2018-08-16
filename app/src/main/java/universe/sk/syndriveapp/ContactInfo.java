package universe.sk.syndriveapp;

public class ContactInfo
{
    public String cName1;
    public String cName2;
    public String cName3;
    public String cNum1;
    public String cNum2;
    public String cNum3;

    public ContactInfo() {

    }
    public ContactInfo(String cName1, String cName2, String cName3, String cNum1, String cNum2, String cNum3) {
        this.cName1 = cName1;
        this.cName2 = cName2;
        this.cName3 = cName3;
        this.cNum1 = cNum1;
        this.cNum2 = cNum2;
        this.cNum3 = cNum3;
    }

    public String getcName1() {
        return cName1;
    }
    public void setcName1(String cName1) {
        this.cName1 = cName1;
    }

    public String getcName2() {
        return cName2;
    }
    public void setcName2(String cName2) {
        this.cName2 = cName2;
    }

    public String getcName3() {
        return cName3;
    }
    public void setcName3(String cName3) {
        this.cName3 = cName3;
    }

    public String getcNum1() {
        return cNum1;
    }
    public void setcNum1(String cNum1) {
        this.cNum1 = cNum1;
    }

    public String getcNum2() {
        return cNum2;
    }
    public void setcNum2(String cNum2) {
        this.cNum2 = cNum2;
    }

    public String getcNum3() {
        return cNum3;
    }
    public void setcNum3(String cNum3) {
        this.cNum3 = cNum3;
    }

}
