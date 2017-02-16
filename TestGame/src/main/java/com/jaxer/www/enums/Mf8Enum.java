package com.jaxer.www.enums;

import com.jaxer.www.model.SokoMap;

public enum Mf8Enum
{
    //
    space1('-', SokoMap.empty),
    //
    space2(' ', SokoMap.empty),
    //
    wall1('#', SokoMap.wall),
    //
    wall2('H', SokoMap.wall),
    //
    wall3('_', SokoMap.wall),
    //
    goal('.', SokoMap.goal),
    //
    box('$', SokoMap.box),
    //
    player1('@', SokoMap.player),
    //
    player2('a', SokoMap.player),
    //
    playerOnGoal1('x', SokoMap.playerOnGoal),
    //
    playerOnGoal2('+', SokoMap.playerOnGoal),
    //
    boxOnGoal('*', SokoMap.boxOnGoal),
    //
    split1(';', ';'), split2('|', ';')
    
    ;
    public static char error = 'E';
    
    private char source;
    
    private char dect;
    
    Mf8Enum(char source, char dect)
    {
        this.source = source;
        this.dect = dect;
    }
    
    public static char getDect(char source)
    {
        
        for (Mf8Enum t : Mf8Enum.values())
        {
            if (t.source == source)
            {
                return t.dect;
            }
        }
        return error;
    }
}
