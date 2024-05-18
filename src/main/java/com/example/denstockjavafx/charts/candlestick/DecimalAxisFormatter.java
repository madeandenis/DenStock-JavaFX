/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.denstockjavafx.charts.candlestick;

import java.text.DecimalFormat;
import java.text.ParseException;
import javafx.util.StringConverter;

/**
 *
 * @author RobTerpilowski
 */
public class DecimalAxisFormatter extends StringConverter<Number>{

    protected DecimalFormat decimalFormat;
    
    @Override
    public String toString(Number object) {
        return decimalFormat.format(object.doubleValue());
    }

    @Override
    public Number fromString(String string) {
        try {
            return decimalFormat.parse(string);
        } catch (ParseException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    
    
}
