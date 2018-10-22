package LearnLanguages;

import java.awt.*;
import java.net.URLDecoder;

public interface Constants
{
    Dimension DIMENSION     = Toolkit.getDefaultToolkit().getScreenSize();

    double buttonWidthMultiplier = 0.608;
    double buttonHeightMultiplier = 0.104;
    double fontMultiplier = 0.052;
    double FONT_SIZE_MULTIPLIER = 0.052;

    int NUMBER_OF_CHARACTERS_IN_LINE = 60;
    int RUNNABLE_TIMEOUT = 5;
    int SIZE = (int) (DIMENSION.width / 6.4);

    String ACCORDING_TO_DATE_OF_ADDITION = "Według daty dodania";
    String ADD = "+";
    String ADD_LIST = "Dodaj listę";
    String ADD_WORD   = "Dodaj słowo";
    String ADD_WORD_FIRST_PART_OF_INFORMATION = "Wybierz w jaki sposób chcesz";
    String ADD_WORD_SECOND_PART_OF_INFORMATION = "dodać słowo:";
    String ALL_WORDS = "Wszystkie słowa";
    String ALL_LISTS= "allLists";
    String ALPHABETICAL = "alphabetical";
    String AUTOMATICALLY = "Automatycznie";
    String A_Z = "A-Z";

    String CANCEL = "Anuluj";
    String CATEGORY = "Kategoria";
    String CATEGORY_ALREADY_EXISTS = "Kategoria już istnieje!";
    String CATEGORY_NAME_IS_EMPTY = "Nazwa kategorii jest pusta!";
    String CLICK_OK_TO_CONTINUE = "Kliknij ok, aby kontynuować.";
    String CHOOSE_CATEGORY = "Wybierz kategorie";
    String CHOOSE_HOW_TO_SORT = "Wybierz, jak chcesz posortować elementy:";
    String CHOOSE_STARTING_LIST = "Wybierz listę startową";
    String CHOOSE_TARGET_LIST = "Wybierz listę docelową";
    String CHOOSE_WHAT_TO_SORT = "Wybierz, co chcesz poddać sortowaniu:";
    String CHOOSE_WHERE_TO_GO = "Wybierz, gdzie chcesz przejść po zamknięciu okna:";
    String CHOOSE_WORD_TO_MOVE = "Wybierz słowo do przeniesienia";
    String COLON = ":";

    String DATE_OF_ADDITION = "dateOfAddition";
    String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    String DELETE = "-";
    String DELETE_LIST = "Usuń listę";
    String DOWN = "DOWN";

    String EDIT = "Edytuj";
    String ENCODING = "UTF-8";
    String ELEMENT = "Element";
    String ELEMENT_ALREADY_EXISTS = "Element już istnieje!";
    String ELEMENT_NAME_IS_EMPTY = "Nazwa elementu jest pusta!";
    String EMPTY_SPACE = " ";
    String ENTER_SEARCHED_PHRASE = "Wpisz szukaną frazę:";
    String ERROR = "Błąd";
    String EXIT       = "Wyjście";

    String FILE_IS_CORRUPTED = "Plik jest uszkodzony.";
    String FIND = "Znajdź";
    String FIRST_PART_OF_INFORMATION = "Wybierz język, na który chcesz";
    String FIRST_PATTERN_REGEX = "<[dlp]+>(.+?)</[dlp]+>";
    String FIRST_REGEX_FOR_MATCHING = "\\D";
    String FONT_NAME     = "Arial";
    String FONT_TAG_FIRST_PART = "<html><font color='";
    String FONT_TAG_SECOND_PART = "'>";
    String FONT_TAG_THIRD_PART = "</font></html>";

    String GIVEN_DATA_IS_INCOMPLETE_FIRST_PART_OF_INFORMATION = "Wprowadzone dane nie są kompletne!";
    String GIVEN_DATA_IS_INCOMPLETE_SECOND_PART_OF_INFORMATION = "Proszę wpisać brakujące informacje.";
    String GIVEN_WORD_IS_ALREADY_ON_THE_SELECTED_LIST = "Podano słowo już figuruje na wybranej liście!";
    String GREEN = "green";

    String INFORMATION = "Informacja";

    String JAVA_IO_FILE_NOT_FOUND_EXCEPTION = "java.io.FileNotFoundException";
    String JAVA_LANG_ARRAY_INDEX_OUT_OF_BOUND_EXCEPTION = "java.lang.ArrayIndexOutOfBoundsException";
    String JSON_EXTENSION = ".JSON";
    String LEFT = "LEFT";
    String LIST = "Lista";
    String LIST_OF_LISTS = "listOfLists";
    String LISTS_MENU_NAME = "Listy";
    String LIST_WORDS = "Słowa listy";
    String LISTS = "Listy";

    String MANUALLY = "Ręcznie";
    String MEANINGS = "znaczenia:";
    String MENU = "Menu";
    String MODIFY = "✓";
    String MODIFY_LIST = "Modyfikuj listę";
    String MODIFY_WORD = "Modyfikuj słowo";
    String MOVE_DOWN = "˅";
    String MOVE_LIST_DOWN = "Przesuń listę w dół";
    String MOVE_LIST_UP = "Przesuń listę w górę";
    String MOVE_UP = "˄";
    String MOVE_WORD = "Przenieś";
    String MOVE_WORD_DOWN = "Przesuń słowo w dół";
    String MOVE_WORD_TO_ANOTHER_LIST = "Przenieś słowo";
    String MOVE_WORD_UP = "Przesuń słowo w górę";

    String NEW_CATEGORY = "Nowa Kategoria";
    String NEW_ELEMENT = "Nowy element";
    String NEW_LIST = "Nowa Lista";
    String NEXT = "->";
    String NEXT_WORD = "-->";
    String NO_CATEGORY_CHOSEN = "Nie wybrano żadnej kategorii!";
    String NO_INTERNET_CONNECTION = "Brak połączenia z internetem!";
    String NO_LIST_EXISTS = "Nie ma żadnej listy!";
    String NO_RECORDS_FOUND = "Nic nie znaleziono";
    String THERE_IS_NOTHING_TO_SORT = "Nie ma nic do posortowania!";

    String OK = "Ok";
    String ORDER = "order";
    String OUT_OF = " z ";

    String PLEASE_CHOSE_AT_LEAST_ONE_CATEGORY = "Proszę wybrać co najmniej 1 kategorię.";
    String PLEASE_CREATE_A_LIST = "Proszę stworzyć listę";
    String PLEASE_RESTART_PROGRAM = "Proszę uruchomić ponownie program.";
    String PLEASE_WRITE_NEW_WORD_OR_MODIFY_THE_OLD_ONE = "Proszę dodać nowe słowo lub zmodyfikować stare.";
    String PREVIEW = "Podgląd";
    String PREVIOUS = "<-";
    String PREVIOUS_WORD = "<--";
    String PROGRAM_LOCATION = URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    String PROGRAM_NAME = "Learn Languages";

    String RED = "red";
    String REMOVE_WORD = "Usuń słowo";
    String REPLACE = "Zastąp";
    String REVERSE_ALPHABETICAL = "reserveAlphabetical";
    String RIGHT = "RIGHT";

    String SAVE_DATA = "Zapisz dane";
    String SAVE_SETTINGS = "Zapisz";

    String SEARCH = "Szukaj";
    String SEARCH_MENU_NAME = "Szukaj";

    String SECOND_PART_OF_INFORMATION = "tłumaczyć słowa:";
    String SECOND_PATTERN_REGEX = "([\\p{L}\\s]+)(:)";
    String SECOND_REGEX_FOR_MATCHING = "\\s";
    String SEMICOLON = ";";
    String SETTINGS_MENU_NAME = "Ustawienia";
    String SETTINGS = "Ustawienia";
    String SETTINGS_SAVED_SUCCESSFULLY = "Udało się zmienić ustawienia";
    String SETTINGS_SUFFIX = "_SETTINGS";
    String SHOW_LISTS = "Wyświetl listy";
    String SORT_MENU_NAME = "Sortuj";
    String SORT = "Sortuj";
    String SORTING_WAS_SUCCESSFUL = "Sortowanie przebiegło pomyślnie";
    String SOURCES = "źródła&#58;";


    String THERE_HAS_TO_BE_TWO_LISTS = "By użyć opcji muszą istnieć co najmniej 2 listy.";
    String THE_WORD_DOES_NOT_EXIST = "Podane słowo nie istnieje!";
    String THIRD_PATTERN_REGEX = "\\([0-9.]+\\)";
    String TO_USE_OPTION_THERE_HAS_TO_BE_AT_LEAST_ONE_LIST = "By użyć opcji musi istnieć co najmniej 1 lista.";
    String TRANSLATE = "Przetłumacz";

    String UP = "UP";

    String WIKTIONARY_ADDRESS = "https://pl.wiktionary.org/wiki/";
    String WORD = "Słowo";
    String WORD_ALREADY_EXISTS_FIRST_PART_OF_MEESAGE = "Słowo już istnieje!";
    String WORD_ALREADY_EXISTS_SECOND_PART_OF_MEESAGE = "Proszę dodać inne lub zmienić stare.";
    String WORD_HAS_BEEN_SUCCESSFULLY_TRANSLATED = "Słowo przetłumaczono pomyślnie!";
    String WORDS_MENU_NAME = "Słowa";
    String WORD_SUCCESSFULLY_MODIFIED = "Udało się zmodyfikowac słowo!";
    String WORD_SUCCESSFULLY_MOVED = "Słowo zostało przeniesione!";
    String WORD_SUCCESSFULLY_SAVE_IN_GIVEN_LIST = "Udało się zapisać słowo do wybranej listy!";
    String WORDS = "_WORDS";
    String WORDS_OF_ALL_LISTS = "wordsOfAllLists";
    String WORDS_OF_GIVEN_LIST = "wordsOfGivenList";
    String WORDS_SUFFIX = "_WORDS";

    String Z_A = "Z-A";

    String POLISH_LANGUAGE = "język polski";

    String ENGLISH_LANGUAGE = "język angielski";
    String RUSSIAN_LANGUAGE = "język rosyjski";
    String FRENCH_LANGUAGE = "język francuski";
    String ITALIAN_LANGUAGE = "język włoski";
    String SPANISH_LANGUAGE = "język hiszpański";
    String PORTUGAL_LANGUAGE = "język portugalski";
    String ANCIENT_GREEK_LANGUAGE = "język starogrecki";
    String MODERN_GREEK_LANGUAGE = "język nowogrecki";
    String LATIN_LANGUAGE = "język łaciński";
    String GERMAN_LANGUAGE = "język niemiecki";
    String CZECH_LANGUAGE = "język czeski";
    String LITHUANIAN_LANGUAGE = "język litewski";
    String DUTCH_LANGUAGE = "język holenderski";
    String HUNGARIAN_LANGUAGE = "język węgierski";
    String TURKISH_LANGUAGE = "język turecki";
    String FINNISH_LANGUAGE = "język fiński";
    String SWEDISH_LANGUAGE = "język szwedzki";
    String ESTONIAN_LANGUAGE = "język estoński";
    String AFRIKAANS_LANGUAGE = "język afrykanerski";
    String CROATIAN_LANGUAGE = "język chorwacki";

    //język afrykanerski


    String KEY_FOR_FIRST_LANGUAGE = "FIRST_LANGUAGE";
    String KEY_FOR_SECOND_LANGUAGE = "SECOND_LANGUAGE";
}
