/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandParser.parserService.messages;

/**
 *
 * @author julien
 */
public class Command
{
    private Action action;
    private String artist;
    private String title;

    public Command(Action action, String artist, String title)
    {
        this.action = action;
        if(artist != null)
            this.artist = artist.trim();
        else
            this.artist = null;
        if(title != null)
            this.title = title.trim();
        else
            this.title = null;
    }
    
    public void setAction(Action action) { this.action = action; }

    public void setArtist(String artist) { this.artist = artist; }

    public void setTitle(String title) { this.title = title; }

    public Action getAction() { return action; }

    public String getArtist() { return artist; }

    public String getTitle() { return title; }

}
