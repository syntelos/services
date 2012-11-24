/*
 * Services
 * Copyright (C) 2012 John Pritchard
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */
package services;

import java.io.Closeable;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogRecord;

/**
 * A services resource reader for listing class name contents.
 * 
 * @see Classes
 */
public class Names
    extends lxl.ArrayList<ClassName>
{
    public final static String NamePrefix = "META-INF/services/";

    protected final static Logger Log = Logger.getLogger(Names.class.getName());

    public static lxl.List<String> Read(ClassName service)
        throws IOException
    {
        lxl.List<String> re = new lxl.ArrayList<String>();
        String name = NamePrefix+service.getName();
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(name);
        while (resources.hasMoreElements()){
            URL resource = resources.nextElement();
            InputStream in = resource.openStream();
            if (null != in){
                BufferedReader strin = new BufferedReader(new InputStreamReader(in));
                try {
                    String line;
                    while (null != (line = strin.readLine())){
                        int comment = line.indexOf('#');
                        if (-1 != comment)
                            line = line.substring(0,comment);
                        line = line.trim();
                        if (0 != line.length())
                            re.add(line);
                    }
                }
                finally {
                    in.close();
                }
            }
        }
        return re;
    }
    public static lxl.List<String> Read(java.io.File file)
        throws IOException
    {
        lxl.List<String> re = new lxl.ArrayList<String>();
        InputStream in = new java.io.FileInputStream(file);
        if (null != in){
            BufferedReader strin = new BufferedReader(new InputStreamReader(in));
            try {
                String line;
                while (null != (line = strin.readLine())){
                    int comment = line.indexOf('#');
                    if (-1 != comment)
                        line = line.substring(0,comment);
                    line = line.trim();
                    if (0 != line.length())
                        re.add(line);
                }
            }
            finally {
                in.close();
            }
        }
        return re;
    }


    private final ClassName service;

    private final Resource resource;


    public Names(File dir, String className){
        this(dir, (new ClassName(className)));
    }
    public Names(File dir, ClassName service){
        super();
        if (null != service){
            this.service = service;
            this.resource = new Resource(dir,(NamePrefix+service.getName()));
            try {
                InputStream in = resource.openStream();
                if (null != in){
                    try {
                        this.read(in);
                    }
                    finally {
                        in.close();
                    }
                }
            }
            catch (IOException exc){
                LogRecord rec = new LogRecord(Level.SEVERE,this.resource.getPath());
                rec.setThrown(exc);
                Log.log(rec);
            }
        }
        else
            throw new IllegalArgumentException();
    }
    public Names(String className){
        this(new ClassName(className));
    }
    public Names(ClassName service){
        super();
        if (null != service){
            this.service = service;
            this.resource = new Resource(NamePrefix+service.getName());
            try {
                Enumeration<URL> resources = this.resource.scanfor();
                while (resources.hasMoreElements()){
                    URL resource = resources.nextElement();
                    InputStream in = resource.openStream();
                    try {
                        this.read(in);
                    }
                    finally {
                        in.close();
                    }
                }
            }
            catch (IOException exc){
                LogRecord rec = new LogRecord(Level.SEVERE,this.resource.getPath());
                rec.setThrown(exc);
                Log.log(rec);
            }
        }
        else
            throw new IllegalArgumentException();
    }


    public void add(String name){

        ClassName clas = new ClassName(name);
        if (!this.contains(clas)){
            this.add(clas);
        }
    }
    public final ClassName getService(){
        return this.service;
    }
    public final Resource getResource(){
        return this.resource;
    }
    public final String getServiceName(){
        return this.service.getName();
    }
    public final String getName(){
        return this.resource.getPath();
    }
    public boolean dropTouch(){
        return this.resource.dropTouch();
    }
    public File getParentFile(){
        return this.resource.getParentFile();
    }
    /**
     * Indempotent initializer will get called more than once, but
     * should only eval once.
     */
    public Names init(){
        return this;
    }
    public boolean write() throws IOException {
        OutputStream out = this.resource.openOutput();
        if (null != out){
            try {
                PrintWriter services = new PrintWriter(new OutputStreamWriter(out,"US-ASCII"));
                for (ClassName name: this){
                    services.println(name.toString());
                }
                services.flush();
                return true;
            }
            finally {
                out.flush();
                out.close();
            }
        }
        return false;
    }
    private int read(InputStream in) throws IOException {
        int count = 0;
        if (null != in){
            BufferedReader strin = new BufferedReader(new InputStreamReader(in));
            String line;
            while (null != (line = strin.readLine())){
                int comment = line.indexOf('#');
                if (-1 != comment)
                    line = line.substring(0,comment);
                line = line.trim();
                if (0 != line.length()){
                    this.add(line);
                    count += 1;
                }
            }
        }
        return count;
    }
}
