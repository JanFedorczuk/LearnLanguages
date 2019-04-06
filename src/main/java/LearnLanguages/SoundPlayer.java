package LearnLanguages;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import javax.sound.sampled.*;
import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.toIntExact;

public class SoundPlayer
{
    private String state = Constants.STOPPED;

    private List<String> filesNames;
    private WordFrame wordFrame;
    private String firstPartOfFilePath;

    private String fileName;
    private String filePath;

    private Clip clip;
    private AudioInputStream audioInputStream;

    private long position;

    private Thread timerThread;
    private Timer timer;

    public SoundPlayer(List<String> filesNames, WordFrame wordFrame, String firstPartOfFilePath)
    {
        this.filesNames = filesNames;
        this.wordFrame = wordFrame;
        this.firstPartOfFilePath = firstPartOfFilePath;
        this.fileName = filesNames.get(0);
        this.filePath = firstPartOfFilePath + filesNames.get(0) + Constants.WAV_SUFFIX;

        clip = getNewClip();
    }

    public void action()
    {
        if(state == Constants.STOPPED)
        {
            start(0);
        }
        else if(state == Constants.PLAYING)
        {
            pause();
        }
        else if(state == Constants.PAUSED)
        {
            resume();
        }
    }

    public Clip getNewClip()
    {
        File audioFile = new File(Constants.PROGRAM_LOCATION + filePath);

        Clip newClip = null;

        try
        {
            try
            {
                clip.close();
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }

            try
            {
                audioInputStream.close();
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }

            audioInputStream = AudioSystem.getAudioInputStream(audioFile);

            newClip = AudioSystem.getClip();
            newClip.open(audioInputStream);
            newClip.addLineListener(new LineListener()
            {
                @Override
                public void update(LineEvent event)
                {
                    if(event.getType() == LineEvent.Type.START)
                    {
                        wordFrame.setPlayOrPauseButtonText(Constants.PAUSE_SOUND);
                    }
                    else if(event.getType() == LineEvent.Type.STOP)
                    {
                        cancelTimer();
                        wordFrame.setPlayOrPauseButtonText(Constants.PLAY_SOUND);

                        if(state != Constants.PAUSED)
                        {
                            if(clip.getFramePosition() >= clip.getFrameLength())
                            {
                                chooseNextFile();
                            }
                            else
                            {
                                state = Constants.STOPPED;
                                wordFrame.setTimeSliderValue(0);
                            }
                        }
                    }
                    else if(event.getType() == LineEvent.Type.CLOSE)
                    {
                        if(state == Constants.STOPPED)
                        {
                            wordFrame.setTimeSliderValue(0);
                        }
                    }
                }
            });
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return newClip;
    }

    public void start(long startingPosition)
    {
        try
        {
            state = Constants.PLAYING;

            clip = getNewClip();

            timerThread = null;
            timerThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    timer = null;
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            long duration = clip.getMicrosecondPosition() / 1000000;
                            int value = (int) Math.floor(duration);
                            wordFrame.setTimeSliderValue(value);
                        }
                    }, 50, 1000);

                }
            });

            if(startingPosition != -1)
            {
                clip.setMicrosecondPosition(startingPosition);
            }

            clip.start();

            timerThread.start();

            wordFrame.setPlayOrPauseButtonText(Constants.PAUSE_SOUND);

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void stop()
    {
        try
        {
            state = Constants.STOPPED;

            clip.stop();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void stopClipFromSlider()
    {
        try
        {
            state = Constants.PAUSED;

            clip.stop();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void pause()
    {
        try
        {
            state = Constants.PAUSED;

            position = clip.getMicrosecondPosition() - 350000;
            clip.stop();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void resume()
    {
        try
        {
            state = Constants.PLAYING;

            clip.setMicrosecondPosition(position);
            timerThread = null;
            timerThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    timer = null;
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            long duration = clip.getMicrosecondPosition() / 1000000;
                            int value = (int) Math.floor(duration);
                            wordFrame.setTimeSliderValue(value);
                        }
                    }, 50, 1000);
                }
            });

            clip.start();

            timerThread.start();

            wordFrame.setPlayOrPauseButtonText(Constants.PAUSE_SOUND);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void cancelTimer()
    {
        try
        {
            if(timer != null)
            {
                timer.cancel();
                timer = null;
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void setPlayerState()
    {
        try
        {
            wordFrame.setFileName(fileName);
            wordFrame.setMaxTimeSliderValue(getFileDuration(fileName));
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void setSliderMaxValue()
    {
        try
        {
            wordFrame.setMaxTimeSliderValue(getFileDuration(fileName));

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private int getFileDuration(String name)
    {
        File file = new File(Constants.PROGRAM_LOCATION + firstPartOfFilePath + name + Constants.WAV_SUFFIX);

        int fileDuration = 0;

        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat audioFormat = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            double duration = (frames+ 0.0) / audioFormat.getFrameRate();
            fileDuration = (int) duration;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return fileDuration;
    }

    public void chooseNextFile()
    {
        try
        {
            int index = filesNames.indexOf(fileName) + 1;

            if(checkIfIndexOfListRepresentsItsElement(Constants.NEXT))
            {
                transitionAction(index);
            }
            else
            {
                if(state == Constants.PLAYING)
                {
                    stop();
                }
                else if(state == Constants.PAUSED)
                {
                    wordFrame.setTimeSliderValue(0);
                }

                wordFrame.setPlayOrPauseButtonText(Constants.PLAY_SOUND);
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void choosePreviousFile()
    {
        try
        {
            int index = filesNames.indexOf(fileName) - 1;

            if(checkIfIndexOfListRepresentsItsElement(Constants.PREVIOUS))
            {
                transitionAction(index);
            }
            else
            {
                if(state == Constants.PLAYING)
                {
                    stop();
                }

                wordFrame.setPlayOrPauseButtonText(Constants.PLAY_SOUND);
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void transitionAction(int index)
    {
        try
        {
            fileName = filesNames.get(index);
            filePath = firstPartOfFilePath + filesNames.get(index) + Constants.WAV_SUFFIX;

            String previousState = state.toString();

            if(state == Constants.PLAYING)
            {
                stop();
            }
            else if(state == Constants.PAUSED)
            {
                wordFrame.setTimeSliderValue(0);
                position = 0;
            }

            clip = getNewClip();

            if(previousState == Constants.PLAYING)
            {
                start(0);
            }

            setPlayerState();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private boolean checkIfIndexOfListRepresentsItsElement(String type)
    {
        try
        {
            if(type == Constants.NEXT)
            {
                int index = filesNames.indexOf(fileName) + 1;

                if(index <= filesNames.size() - 1)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                int index = filesNames.indexOf(fileName) - 1;

                if(index != -1)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            return false;
        }
    }

    public String getState()
    {
        return state;
    }

    public Clip getClip()
    {
        return clip;
    }

    public void closeClipAndAudioInputStream()
    {
        clip.close();

        try
        {
            audioInputStream.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
