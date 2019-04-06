package LearnLanguages;

import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;

public class TextFieldLimit extends PlainDocument
{
    private int limit;

    public TextFieldLimit(int limit)
    {
        super();
        this.limit = limit;
    }

    public void insertString(int offset, String  string, AttributeSet attr)
    {
        try
        {
            if (string == null) return;

            if ((getLength() + string.length()) <= limit)
            {
                super.insertString(offset, string, attr);
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}