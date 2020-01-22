/*
 * Copyright 1997-2020 Unidata Program Center/University Corporation for
 * Atmospheric Research, P.O. Box 3000, Boulder, CO 80307,
 * support@unidata.ucar.edu.
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package ucar.visad.quantities;



import visad.*;


/**
 * Provides support for the quantity of CAPE (Convective Available Potential
 * Energy).
 *
 * @author Steven R. Emmerson
 * @version $Id: CAPE.java,v 1.10 2005/05/13 18:35:37 jeffmc Exp $
 */
public class CAPE extends ScalarQuantity {

    /** The single instance */
    private static CAPE instance;

    static {
        try {
            instance = new CAPE();
        } catch (Exception e) {
            System.err.println("Couldn't initialize class");
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
    }

    /**
     * Default constructor
     *
     * @throws UnitException   unit problem (shouldn't happen)
     * @throws VisADException  VisAD error
     */
    private CAPE() throws UnitException, VisADException {
        super("CAPE", SI.meter.divide(SI.second).pow(2), (Set) null);
    }

    /**
     * Returns the VisAD {@link RealType} associated with this instance.
     *
     * @return             The associated {@link RealType}.
     * @throws VisADException if a core VisAD failure occurs.
     */
    public static RealType getRealType() throws VisADException {
        return instance.realType();
    }
}
