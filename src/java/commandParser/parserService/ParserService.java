/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandParser.parserService;

import commandParser.parserService.messages.Action;
import commandParser.parserService.messages.Command;
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
        String[] words = request.split(" ");
        if(words.length < 2)
            return null;
        
        return new Command(Action.SEARCH, "paul", null);
    }
}