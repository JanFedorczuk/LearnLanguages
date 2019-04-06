package LearnLanguages;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TranslatorRunnable implements Runnable
{
    private String word;
    private String dataString;
    private String languageToBeTranslated;

    private List<Object> informationList = new ArrayList<>();

    @Override
    public void run()
    {
        informationList = checkIfWordExistsAndCreateInformationList();
    }

    public void setValues(String word, String languageToBeTranslated)
    {
        this.word = word;
        this.languageToBeTranslated = languageToBeTranslated;
    }

    public List<Object> checkIfWordExistsAndCreateInformationList()
    {
        InputStream inputStream = null;

        List<Object> list = new ArrayList<>();

        try
        {
            dataString = downloadData();

            try
            {
                String[] stringPartsStartingFrom = dataString.split("\\(" + languageToBeTranslated + "\\)");
                String[] stringPartsEndingAt = stringPartsStartingFrom[1].split(Constants.SOURCES);

                list.add(true);
                list.add(null);
            }
            catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException)
            {
                arrayIndexOutOfBoundsException.printStackTrace();

                list.add(false);
                list.add(arrayIndexOutOfBoundsException);
            }
            catch (IndexOutOfBoundsException indexOutOfBoundsException)
            {
                indexOutOfBoundsException.printStackTrace();

                list.add(false);
                list.add(indexOutOfBoundsException);
            }
        }
        catch(FileNotFoundException fileNotFoundException)
        {
            fileNotFoundException.printStackTrace();

            list.add(false);
            list.add(fileNotFoundException);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();

            list.add(false);
            list.add(exception);
        }
        finally
        {
            try
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }
            }
            catch (IOException iOException)
            {
                iOException.printStackTrace();
            }
        }

        return list;
    }

    private String downloadData() throws Exception
    {
        InputStream inputStream = null;
        try
        {
            word = URLEncoder.encode(word, Constants.ENCODING);

            String urlString = Constants.WIKTIONARY_ADDRESS + word;

            URL url = new URL(urlString);

            inputStream = url.openStream();
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuffer stringBuffer = new StringBuffer();


            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer = stringBuffer.append(line);
            }

            return new String(stringBuffer);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            throw exception;
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
    }

    public List<Object> getList()
    {

        return informationList;
    }

    public Boolean getBooleanValue()
    {

        return (Boolean) informationList.get(0);
    }

    public String getDataString()
    {
        return dataString;
    }
}
