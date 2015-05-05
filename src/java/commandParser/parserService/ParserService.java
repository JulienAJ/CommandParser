/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandParser.parserService;

import commandParser.parserService.messages.Action;
import commandParser.parserService.messages.Command;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

/**
 *
 * @author julien
 */
@WebService(serviceName = "ParserService", targetNamespace = "http://parserServicer.commandParser/ParserService")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
public class ParserService
{
    /**
     * Web service operation
     * @param request
     * @return 
     */
    @WebMethod(operationName = "parse")
    public Command parse(@WebParam(name = "request") String request)
    {
        //TODO write your implementation code here:
        // SOUNDEX
        String[] addWords = {"ajout de", "ajouter", "rajouter", "ajoutes", "rajoutes"};
        String[] deleteWords = {"suppression de", "supprimer", "retirer", "enlever", "supprimes", "retires", "enlèves"};
        String[] playWords = {"écouter", "jouer", "lancer", "joues", "lances"};
        String[] searchWords = {"chercher", "recherche de", "rechercher", "cherches", "recherches", "trouves"};
        
        String[] titleArtistSeparators = {"de", "par"};
        
        //Pattern[] addPatterns = new Pattern[addWords.length * titleArtistSeparators.length];
        List<Pattern> addPatterns = new ArrayList<Pattern>();
        List<Pattern> addPatterns2 = new ArrayList<Pattern>();
        for(String word : addWords)
        {
            for(String sep : titleArtistSeparators)
            {
                String temp = "^" + word + "(( (.+))?( " + sep + "(.+)))?$";
                addPatterns.add(Pattern.compile(temp));
            }
            String temp = "^" + word + "( (.+))$";
            addPatterns2.add(Pattern.compile(temp));
        }
        
        List<Pattern> removePatterns = new ArrayList<Pattern>();
        List<Pattern> removePatterns2 = new ArrayList<Pattern>();
        for(String word : deleteWords)
        {
            for(String sep : titleArtistSeparators)
            {
                String temp = "^" + word + "(( (.+))?( " + sep + "(.+)))?$";
                removePatterns.add(Pattern.compile(temp));
            }
            String temp = "^" + word + "( (.+))$";
            removePatterns2.add(Pattern.compile(temp));
        }
        
        List<Pattern> playPatterns = new ArrayList<Pattern>();
        List<Pattern> playPatterns2 = new ArrayList<Pattern>();
        for(String word : playWords)
        {
            for(String sep : titleArtistSeparators)
            {
                String temp = "^" + word + "(( (.+))?( " + sep + "(.+)))?$";
                playPatterns.add(Pattern.compile(temp));
            }
            String temp = "^" + word + "( (.+))$";
            playPatterns2.add(Pattern.compile(temp));
        }
        
        List<Pattern> searchPatterns = new ArrayList<Pattern>();
        for(String word : searchWords)
        {
            String temp = "^" + word + "(( (.+))?( dans l'artiste))$";
            searchPatterns.add(Pattern.compile(temp));
            temp = "^" + word + "(( (.+))?( dans le titre))$";
            searchPatterns.add(Pattern.compile(temp));
        }
        
        Action action = null;
        String title = null;
        String artist = null;
        
        for(Pattern p : addPatterns)
        {
            Matcher match = p.matcher(request);
            if(match.find())
            {
                action = Action.ADD;
                title = match.group(2);
                if(match.group(5) != null)
                    artist = match.group(5);
                return new Command(action, artist, title);
            }
        }
        for(Pattern p : addPatterns2)
        {
            Matcher match = p.matcher(request);
            if(match.find())
            {
                action = Action.ADD;
                title = match.group(2);
                return new Command(action, null, title);
            }
        }
        for(Pattern p : removePatterns)
        {
            Matcher match = p.matcher(request);
            if(match.find())
            {
                action = Action.REMOVE;
                title = match.group(2);
                if(match.group(5) != null)
                    artist = match.group(5);
                return new Command(action, artist, title);
            }
        }
        for(Pattern p : removePatterns2)
        {
            Matcher match = p.matcher(request);
            if(match.find())
            {
                action = Action.REMOVE;
                title = match.group(2);
                return new Command(action, null, title);
            }
        }
        for(Pattern p : playPatterns)
        {
            Matcher match = p.matcher(request);
            if(match.find())
            {
                action = Action.PLAY;
                title = match.group(2);
                if(match.group(5) != null)
                    artist = match.group(5);
                return new Command(action, artist, title);
            }
        }
        for(Pattern p : playPatterns2)
        {
            Matcher match = p.matcher(request);
            if(match.find())
            {
                action = Action.PLAY;
                title = match.group(2);
                return new Command(action, null, title);
            }
        }
        for(Pattern p : searchPatterns)
        {
            Matcher match = p.matcher(request);
            if(match.find())
            {
                action = Action.SEARCH;
                if(match.group(4).equals("titre"))
                    title = match.group(3);
                else
                    artist = match.group(3);
                return new Command(action, artist, title);
            }
        }
        
        action = Action.SEARCH;
        title = "error";
        artist = "error";
        
        return new Command(action, artist, title);
    }
}