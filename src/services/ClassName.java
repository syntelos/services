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

import java.io.File;

/**
 * Typed string used is independent contents for one of either fully
 * qualified JVM class names, or unqualified Big Table Kind names.
 */
public final class ClassName
    extends Object
    implements java.lang.Comparable<ClassName>
{


    public final String name, unqualified, pkg;
    public final boolean qualified;


    public ClassName(Resource resource){
        this(resource.file);
    }
    public ClassName(File resource){
        super();
        if (null != resource && resource.isFile()){
            String name = resource.getName();
            int idx = name.lastIndexOf('.');
            if (-1 != idx)
                this.name = name.substring(0,idx);
            else
                this.name = name;

            this.pkg = null;
            this.qualified = false;
            this.unqualified = name;
        }
        else
            throw new IllegalArgumentException();
    }
    public ClassName(String name){
        super();
        if (null != name && 0 != name.length()){
            this.name = name;
            int idx = name.lastIndexOf('.');
            this.qualified = (0 < idx);
            if (this.qualified){
                this.pkg = name.substring(0,idx);
                this.unqualified = name.substring(idx+1);
            }
            else {
                this.pkg = null;
                this.unqualified = name;
            }
        }
        else
            throw new IllegalArgumentException(name);
    }


    public String getName(){
        return this.name;
    }
    public boolean isQualified(){
        return this.qualified;
    }
    public String toString(){
        return this.name;
    }
    public int hashCode(){
        return this.name.hashCode();
    }
    public boolean equals(Object that){
        if (this == that)
            return true;
        else if (null != that)
            return this.name.equals(that.toString());
        else
            return false;
    }
    public int compareTo(ClassName that){
        return this.name.compareTo(that.getName());
    }

}
