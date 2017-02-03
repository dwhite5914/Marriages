package matching;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Matching
{
    public static void main(String[] args)
    {
        String path = getResource("data.txt");

        Marriages marriages = new Marriages(path);
        System.out.println("Men proposing to women:");
        marriages.printMatchesMen();
        System.out.println();
        System.out.println("Women proposing to men:");
        marriages.printMatchesWomen();
    }

    public static String getResource(String name)
    {
        URL url = Matching.class.getResource("resources/" + name);
        File file;
        try
        {
            file = new File(url.toURI());
        }
        catch (URISyntaxException e)
        {
            file = new File(url.getPath());
        }
        return file.getAbsolutePath();
    }
}

class Marriages
{
    private final List<String> men;
    private final List<String> women;
    private final Map<String, List<String>> menPrefs;
    private final Map<String, List<String>> womenPrefs;

    public Marriages(String path)
    {
        men = new ArrayList<>();
        women = new ArrayList<>();
        menPrefs = new HashMap<>();
        womenPrefs = new HashMap<>();

//        men.add("Man1");
//        men.add("Man2");
//        men.add("Men3");
//
//        women.add("Woman1");
//        women.add("Woman2");
//        women.add("Woman3");
//
//        List<String> man1Prefs = new ArrayList<>();
//        man1Prefs.add("Woman1");
//        man1Prefs.add("Woman2");
//        man1Prefs.add("Woman3");
//        menPrefs.put("Man1", man1Prefs);
//
//        List<String> man2Prefs = new ArrayList<>();
//        man2Prefs.add("Woman1");
//        man2Prefs.add("Woman3");
//        man2Prefs.add("Woman2");
//        menPrefs.put("Man2", man2Prefs);
//
//        List<String> man3Prefs = new ArrayList<>();
//        man3Prefs.add("Woman3");
//        man3Prefs.add("Woman2");
//        man3Prefs.add("Woman1");
//        menPrefs.put("Man3", man3Prefs);
//
//        List<String> woman1Prefs = new ArrayList<>();
//        woman1Prefs.add("Man2");
//        woman1Prefs.add("Man1");
//        woman1Prefs.add("Man3");
//        womenPrefs.put("Woman1", woman1Prefs);
//
//        List<String> woman2Prefs = new ArrayList<>();
//        woman2Prefs.add("Man1");
//        woman2Prefs.add("Man3");
//        woman2Prefs.add("Man2");
//        womenPrefs.put("Woman2", woman2Prefs);
//
//        List<String> woman3Prefs = new ArrayList<>();
//        woman3Prefs.add("Man3");
//        woman3Prefs.add("Man1");
//        woman3Prefs.add("Man2");
//        womenPrefs.put("Woman3", woman3Prefs);
        loadData(path);
    }

    private void loadData(String path)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(path)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                // Split line into components.
                String components[] = line.split(" ");
                int numPeople = components.length - 1;

                // Get person and their prefs from line.
                String person = components[0];
                List<String> prefs = Arrays.asList(
                        Arrays.copyOfRange(components, 1, components.length));

                // Populate women list if empty.
                if (women.isEmpty())
                {
                    women.addAll(prefs);
                }

                // Add man/woman to appropriate lists.
                boolean isMan = !women.contains(person);
                if (isMan)
                {
                    men.add(person);
                    menPrefs.put(person, prefs);
                }
                else
                {
                    womenPrefs.put(person, prefs);
                }
            }

            reader.close();
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Failed to find data file.");
        }
        catch (IOException ex)
        {
            System.out.println("Failed to read data from file.");
        }
    }

    public void printMatchesMen()
    {
        Map<String, String> matches = new TreeMap<>();

        // All men start off free.
        List<String> freeMen = new LinkedList<>();
        freeMen.addAll(men);

        // While there are still free men...
        while (!freeMen.isEmpty())
        {
            String man = freeMen.remove(0);
            List<String> currentManPrefs = menPrefs.get(man);

            for (String woman : currentManPrefs)
            {
                if (matches.get(woman) == null)
                {
                    matches.put(woman, man);
                    break;
                }
                else
                {
                    String otherMan = matches.get(woman);
                    List<String> currentWomanPrefs = womenPrefs.get(woman);
                    if (currentWomanPrefs.indexOf(man) < currentWomanPrefs.indexOf(otherMan))
                    {
                        matches.put(woman, man);
                        freeMen.add(otherMan);
                        break;
                    }
                }
            }
        }

        for (String woman : matches.keySet())
        {
            String man = matches.get(woman);
            System.out.println(man + "---" + woman);
        }
    }

    public void printMatchesWomen()
    {
        Map<String, String> matches = new TreeMap<>();

        // All women start off free.
        List<String> freeWomen = new LinkedList<>();
        freeWomen.addAll(women);

        // While there are still free women...
        while (!freeWomen.isEmpty())
        {
            String woman = freeWomen.remove(0);
            List<String> currentWomanPrefs = womenPrefs.get(woman);

            for (String man : currentWomanPrefs)
            {
                if (matches.get(man) == null)
                {
                    matches.put(man, woman);
                    break;
                }
                else
                {
                    String otherWoman = matches.get(man);
                    List<String> currentMenPrefs = menPrefs.get(man);
                    if (currentMenPrefs.indexOf(woman) < currentMenPrefs.indexOf(otherWoman))
                    {
                        matches.put(man, woman);
                        freeWomen.add(otherWoman);
                        break;
                    }
                }
            }
        }

        for (String man : matches.keySet())
        {
            String woman = matches.get(man);
            System.out.println(woman + "---" + man);
        }
    }
}
