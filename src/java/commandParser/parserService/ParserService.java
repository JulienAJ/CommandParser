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
    private Pattern generatePattern(String[] words, String[] separators)
    {
        String wordsString = "(";
        for(String word : words)
        {
            wordsString += (word + '|');
        }
        wordsString = wordsString.substring(0, wordsString.length() - 1) + ')';
        
        String sepString = null;
        if(separators != null && separators.length != 0)
        {
            sepString = "(";
            for(String word : separators)
            {
                sepString += (word + '|');
            }
            sepString = sepString.substring(0, sepString.length() - 1) + ')';
        }
        
        String finalString;
        if(sepString == null)
        {
            finalString = "^" + wordsString + "(.+)$";
        }
        else
        {
            finalString = "^" + wordsString + "(.+)" + sepString + "(.+)$";
        }
        return Pattern.compile(finalString);
    }
    
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
          
        // ADD
        Pattern p = generatePattern(addWords, titleArtistSeparators);
        Matcher match = p.matcher(request);
        if(match.find())
        {
            return new Command(Action.ADD, match.group(4), match.group(2));
        }
        p = generatePattern(addWords, null);
        match = p.matcher(request);
        if(match.find())
        {
            return new Command(Action.ADD, null, match.group(2));
        }
        
        // REMOVE
        p = generatePattern(deleteWords, titleArtistSeparators);
        match = p.matcher(request);
        if(match.find())
        {
            return new Command(Action.REMOVE, match.group(4), match.group(2));
        }
        p = generatePattern(deleteWords, null);
        match = p.matcher(request);
        if(match.find())
        {
            return new Command(Action.REMOVE, null, match.group(2));
        }
        
        // PLAY
        p = generatePattern(playWords, titleArtistSeparators);
        match = p.matcher(request);
        if(match.find())
        {
            return new Command(Action.PLAY, match.group(4), match.group(2));
        }
        p = generatePattern(playWords, null);
        match = p.matcher(request);
        if(match.find())
        {
            return new Command(Action.PLAY, null, match.group(2));
        }
        
        // SEARCH
        p = generatePattern(searchWords, titleArtistSeparators);
        match = p.matcher(request);
        if(match.find())
        {
            return new Command(Action.SEARCH, match.group(4), match.group(2));
        }
        p = generatePattern(searchWords, null);
        match = p.matcher(request);
        if(match.find())
        {
            return new Command(Action.SEARCH, null, match.group(2));
        }
        
        //return new Command(Action.SEARCH, "error", "error");
        return null;
    }
}