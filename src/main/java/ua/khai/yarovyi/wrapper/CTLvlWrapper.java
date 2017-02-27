package ua.khai.yarovyi.wrapper;


import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.math.BigInteger;

/**
 * Wrapper for  {@link org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl CTLvl}
 */
public class CTLvlWrapper {

    /**
     * Prepare base CTLvl with default .docx styles.
     *
     * @param abstractNum list in which CTLvl should be added
     * @param step        position on which it should be added
     * @return initialized object
     */
    public static CTLvl prepareCtlvl(XWPFAbstractNum abstractNum, int step) {
        CTLvl lvl = abstractNum.getCTAbstractNum().addNewLvl();
        lvl.setIlvl(BigInteger.valueOf(0));
        CTDecimalNumber ctDecimalNumber = CTDecimalNumber.Factory.newInstance();
        ctDecimalNumber.setVal(BigInteger.valueOf(step));
        lvl.setStart(ctDecimalNumber);
        CTNumFmt ctNumFmt = CTNumFmt.Factory.newInstance();
        ctNumFmt.setVal(STNumberFormat.DECIMAL);
        lvl.setNumFmt(ctNumFmt);
        CTLevelText ctLevelText = CTLevelText.Factory.newInstance();
        ctLevelText.setVal("%" + step + ".");
        lvl.setLvlText(ctLevelText);
        return lvl;
    }
}
