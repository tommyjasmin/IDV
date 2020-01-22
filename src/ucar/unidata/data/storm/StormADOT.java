/*
 * $Id: IDV-Style.xjs,v 1.1 2006/05/03 21:43:47 dmurray Exp $
 *
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


package ucar.unidata.data.storm;


import ucar.unidata.data.grid.GridUtil;
import ucar.unidata.util.IOUtil;
import ucar.unidata.util.StringUtil;
import visad.FlatField;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: yuanho
 * Date: Feb 20, 2009
 * Time: 3:09:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class StormADOT {

    /** _more_ */
    StormADOTInfo.IRData odtcurrent_v72IR;

    /** _more_ */
    StormADOTInfo.DataGrid areadata_v72;


    /** _more_ */
    boolean lauto = false;

    /** _more_ */
    int idomain_v72, ixdomain_v72, ifixtype_v72, rmwsizeman_v72;

    /** _more_ */
    int oland_v72;

    /** _more_ */
    boolean osearch_v72;

    /** _more_ */
    int ostartstr_v72;

    /** _more_ */
    float osstr_v72;

    /** _more_          */
    static double c1 = 1.191066E-5;

    /** _more_          */
    static double c2 = 1.438833;

    public StormADOT() {

    }

    /**
     * _more_
     *
     *
     * @param satgrid _more_
     * @param cenlat _more_
     * @param cenlon _more_
     * @param posm _more_
     * @param curdate _more_
     * @param cursat _more_
     * @param g_domain _more_
     *
     * @return _more_
     */
    public StormADOTInfo.IRData aodtv72_drive(FlatField satgrid, float cenlat, float cenlon,
                      int posm, double curdate, int cursat,
                      String g_domain, int satId, int satChannel,
                      boolean isTemperature) {

        float ftmps, flats, flons, cenlon2;

        int   radius, irad, np, ii, jj, length;
        int   idomain = 0;


        /*
         *  Set miscoptions flags in AODT
         */

        int eyeSize = -99;
        oland_v72      = 0;        /* allow AODT operation over land */
        osearch_v72    = false;  /* search for maximum curved band position */
        rmwsizeman_v72 = eyeSize;  /* eye size parameter */
        odtcurrent_v72IR = new StormADOTInfo.IRData();
        odtcurrent_v72IR.domain = idomain_v72;

        /*
         *  Set initial classification flag and value in AODT
         */

        ostartstr_v72 = 0;     /* user defined initial classification flag */
        osstr_v72     = 0.0f;  /* starting initial classification value */


        /*
         *  Set image date/time info in AODT
         */

        int iaodt = aodtv72_setIRimageinfo(curdate,  cursat);


        /*
          * Get storm center lat/lon
          */
        if (lauto == true) {
            //   aodtv72_runautomode( nauto, fauto, imagefile, &cenlat, &cenlon, &posm  );
        }


        /*
        *  Set center location in AODT
        *  positioning method (1=interpolation, 4=extrapolation, 0=error)
        */
        //posm  = 1;
       aodtv72_setlocation(cenlat, cenlon, posm);


        /*
         *  Set domain FLAG in AODT
         */
        if (g_domain.equalsIgnoreCase("AUTO")) {
            idomain = 0;
        }
        if (g_domain.equalsIgnoreCase("Atlantic")) {
            idomain = 1;
        }
        if (g_domain.equalsIgnoreCase("Pacific")) {
            idomain = 2;
        }

        iaodt = aodtv72_setdomain(idomain);



        /*
         *  Retrieve temperatures from image. This to be done in IDV
         */
        GridUtil.Grid2D g2d      = null;
        float[][]       temps    = null;
        float[][][]     satimage = null;
        float[][]       lons     = null;
        float[][]       lats     = null;
        int             numx     = 123;
        int             numy     = 123;

        try {
            g2d  = GridUtil.makeGrid2D(satgrid);
            lons = g2d.getlons();
            lats = g2d.getlats();

        } catch (Exception re) {}

        /* now spatial subset numx by numy*/
        GridUtil.Grid2D g2d1 = spatialSubset(g2d, cenlat, cenlon, numx, numy);

        satimage = g2d1.getvalues();
        float[][] temp0  = satimage[0];
        int       imsorc = satId,
                  imtype = satChannel;

        if(isTemperature)
            temps = temp0;
        else
            temps = im_gvtota(numx, numy, temp0, imsorc, imtype);

        /*
         *  Load the IR imge information in AODT
         *  init areadata_v72
         */

        aodtv72_loadIRimage(temps, g2d1.getlats(), g2d1.getlons(),
                                    numx, numy);

        /*
         *  Set eye and cloud temperature values in AODT,
         *  return position for IR image data read
         */
        StormADOTInfo.IRData tvIR = aodtv72_seteyecloudtemp(StormADOTInfo.keyerM_v72,
                areadata_v72);

        odtcurrent_v72IR.warmt = tvIR.warmt;
        odtcurrent_v72IR.warmlatitude = tvIR.warmlatitude;
        odtcurrent_v72IR.warmlongitude = tvIR.warmlongitude;
        odtcurrent_v72IR.eyet = tvIR.eyet;
        odtcurrent_v72IR.cwcloudt = tvIR.cwcloudt;
        odtcurrent_v72IR.cwring = tvIR.cwring;


        /*
         *   Determine scene type
         *   Set scene type
         */

        float[] oscen = ucar.unidata.data.storm.StormADOTSceneType.aodtv72_calcscene(
                                     odtcurrent_v72IR, areadata_v72);

        odtcurrent_v72IR.cloudt = oscen[0];
        odtcurrent_v72IR.cloudt2 = oscen[1];
        odtcurrent_v72IR.eyestdv = oscen[2];
        odtcurrent_v72IR.cloudsymave = oscen[3];
        odtcurrent_v72IR.eyefft = (int)oscen[4];
        odtcurrent_v72IR.cloudfft = (int)oscen[5];
                 // { alst, Aaveext, Estdveye, Aavesym, eyecnt, rngcnt};
        float[] oscen1 = ucar.unidata.data.storm.StormADOTSceneType.aodtv72_classify(
                       odtcurrent_v72IR, rmwsizeman_v72, areadata_v72, osstr_v72, osearch_v72);


        odtcurrent_v72IR.eyescene      = (int) oscen1[0];
        odtcurrent_v72IR.cloudscene    = (int) oscen1[1];
        odtcurrent_v72IR.eyesceneold   = -1;
        odtcurrent_v72IR.cloudsceneold = -1;
        odtcurrent_v72IR.eyecdosize    = oscen1[2];
        odtcurrent_v72IR.ringcb        = (int) oscen1[3];
        odtcurrent_v72IR.ringcbval     = (int) oscen1[4];
        odtcurrent_v72IR.ringcbvalmax  = (int) oscen1[5];
        odtcurrent_v72IR.ringcblatmax  = oscen1[6];
        odtcurrent_v72IR.ringcblonmax  = oscen1[7];
        odtcurrent_v72IR.rmw           = oscen1[8];




        /*
        *   Determine intensity
        */

        iaodt = aodtv72_calcintensity(idomain);
        if (iaodt == 71) {
            throw new IllegalStateException("center location is over land");
        }

        /*
         *   Print out all diagnostic messages to screen
         */
        return odtcurrent_v72IR;
    }



    public static class AdotResult {
    }



    /**
     * _more_
     *
     * @param g2d _more_
     * @param cenlat _more_
     * @param cenlon _more_
     * @param numx _more_
     * @param numy _more_
     *
     * @return _more_
     */
    GridUtil.Grid2D spatialSubset(GridUtil.Grid2D g2d, float cenlat,
                                  float cenlon, int numx, int numy) {
        float[][]   lats    = g2d.getlats();
        float[][]   lons    = g2d.getlons();
        float[][][] values  = g2d.getvalues();
        float[][]   slats   = new float[numx][numy];
        float[][]   slons   = new float[numx][numy];
        float[][][] svalues = new float[1][numx][numy];

        int         ly      = lats[0].length;
        int         ly0     = ly/2;
        int         lx      = lats.length;
        int         lx0     = lx/2;
        int         ii      = numx / 2,
                    jj      = numy / 2;

        for (int j = 0; j < ly-1; j++) {
            if(Float.isNaN(lats[lx0][j])) continue;
            if(j == 209){
                System.out.println(j);
            }
            if ((lats[lx0][j] > cenlat) && (lats[lx0][j + 1] < cenlat)) {
                jj = j;
            }
        }
        for (int i = 0; i < lx-1; i++) {
             if(Float.isNaN(lons[i][ly0])) continue;
            if ((lons[i][ly0] < cenlon) && (lons[i + 1][ly0] > cenlon)) {
                ii = i;
            }
        }
        int startx = ii - (numx / 2 - 1);
        int starty = jj - (numy / 2 - 1);
        if(startx < 0) startx = 0;
        if(starty < 0) starty = 0;

        for (int i = 0; i < numx; i++) {
            for (int j = 0; j < numy; j++) {
                slats[i][j]      = lats[i + startx][j + starty];
                slons[i][j]      = lons[i + startx][j + starty];
                svalues[0][i][j] = values[0][i + startx][j + starty];
            }
        }

        return new GridUtil.Grid2D(slats, slons, svalues);
    }

    /**
     * _more_
     *
     * @param nx _more_
     * @param ny _more_
     * @param gv _more_
     * @param imsorc _more_
     * @param imtype _more_
     *
     * @return _more_
     */
    float[][] im_gvtota(int nx, int ny, float[][] gv, int imsorc, int imtype)

    /**
     * im_gvtota
     *
     * This subroutine converts GVAR counts to actual temperatures
     * based on the current image set in IM_SIMG.
     *
     * im_gvtota ( int *nvals, unsigned int *gv, float *ta, int *iret )
     *
     * Input parameters:
     *      *nvals          int     Number of values to convert
     *      *gv             int     Array of GVAR count values
     *
     * Output parameters:
     *      *ta             float   Array of actual temperatures
     *      *iret           int     Return value
     *                              = -1 - could not open table
     *                              = -2 - could not find match
     *
     *
     * Log:
     * D.W.Plummer/NCEP     02/03
     * D.W.Plummer/NCEP     06/03   Add coeff G for 2nd order poly conv
     * T. Piper/SAIC         07/06   Added tmpdbl to eliminate warning
     */
    {
        int       ii, ip, chan, found, ier;
        double    Rad, Teff, tmpdbl;
        float[][] ta = new float[nx][ny];
        int       iret;
        String    fp = "/ucar/unidata/data/storm/ImgCoeffs.tbl";

        iret = 0;

        for (ii = 0; ii < nx; ii++) {
            for (int jj = 0; jj < ny; jj++) {
                ta[ii][jj] = Float.NaN;
            }
        }

        /*
         *    Read in coefficient table if necessary.
         */
        String s = null;
        try {
            s = IOUtil.readContents(fp);
        } catch (Exception re) {}


        int i = 0;
        StormADOTInfo.ImgCoeffs[] ImageConvInfo =
            new StormADOTInfo.ImgCoeffs[50];
        for (String line : StringUtil.split(s, "\n", true, true)) {
            if (line.startsWith("!")) {
                continue;
            }
            List<String> stoks = StringUtil.split(line, " ", true, true);

            ImageConvInfo[i] = new StormADOTInfo.ImgCoeffs(stoks);;
            i++;
        }
        int nImgRecs = i;
        found = 0;
        ii    = 0;
        while ((ii < nImgRecs) && (found == 0)) {

            tmpdbl = (double) (ImageConvInfo[ii].chan - 1)
                     * (ImageConvInfo[ii].chan - 1);
            chan = G_NINT(tmpdbl);

            if ((imsorc == ImageConvInfo[ii].sat_num) && (imtype == chan)) {
                found = 1;
            } else {
                ii++;
            }

        }

        if (found == 0) {
            iret = -2;
            return null;
        } else {

            ip = ii;
            for (ii = 0; ii < nx; ii++) {
                for (int jj = 0; jj < ny; jj++) {

                    /*
                     *  Convert GVAR count (gv) to Scene Radiance
                     */
                    Rad = ((double) gv[ii][jj] - ImageConvInfo[ip].scal_b) /
                    /*              -------------------------------------                     */
                    ImageConvInfo[ip].scal_m;

                    Rad = Math.max(Rad, 0.0);

                    /*
                     *  Convert Scene Radiance to Effective Temperature
                     */
                    Teff = (c2 * ImageConvInfo[ip].conv_n) /
                    /*               -------------------------------------------------------   */
                    (Math.log(1.0
                              + (c1 * Math.pow(ImageConvInfo[ip].conv_n,
                                  3.0)) / Rad));

                    /*
                     *  Convert Effective Temperature to Temperature
                     */
                    ta[ii][jj] = (float) (ImageConvInfo[ip].conv_a
                                          + ImageConvInfo[ip].conv_b * Teff
                                          + ImageConvInfo[ip].conv_g * Teff
                                            * Teff);
                }

            }

        }

        return ta;

    }

    /**
     * _more_
     *
     * @param x _more_
     *
     * @return _more_
     */
    public int G_NINT(double x) {
        return (((x) < 0.0F)
                ? ((((x) - (float) ((int) (x))) <= -.5f)
                   ? (int) ((x) - .5f)
                   : (int) (x))
                : ((((x) - (float) ((int) (x))) >= .5f)
                   ? (int) ((x) + .5f)
                   : (int) (x)));
    }

    /**
     * _more_
     *
     * @param keyerM_v72 _more_
     * @param areadata _more_
     *
     * @return _more_
     */
    ucar.unidata.data.storm.StormADOTInfo.IRData aodtv72_seteyecloudtemp(
            int keyerM_v72,
            ucar.unidata.data.storm.StormADOTInfo.DataGrid areadata)
    /* Routine to search for, idenfify, and set the eye and cloud temperature values
       for the AODT library.  Temperatuers are set within AODT library.
       Inputs : none
       Outputs: none
       Return : -51 : eye, CWcloud, or warmest temperature <-100C or >+40C
                  0 : o.k.
    */
    {
        ucar.unidata.data.storm.StormADOTInfo.IRData ird =
            ucar.unidata.data.storm.StormADOTSceneType.aodtv72_gettemps(
                keyerM_v72, areadata);
        if (ird == null) {
            throw new IllegalStateException(
                "eye, CWcloud, or warmest temperature <-100C or >+40C");
        }

        return ird;

        // return iok;
    }

    /**
     * _more_
     *
     * @param temps _more_
     * @param lats _more_
     * @param lons _more_
     * @param numx _more_
     * @param numy _more_
     *
     * @return _more_
     */
    public int aodtv72_loadIRimage(float[][] temps, float[][] lats, float[][] lons,
                            int numx, int numy)
    /* Subroutine to load IR image data grid values (temperatures and positions) into
       data structure for AODT library
       Inputs : temperature, latitude, and longitude arrays centered on storm position location
                along with number of columns (x) and rows (y) in grid
       Outputs: none (areadata_v72 structure passed via global variable)
       Return : 0 : o.k.
    */
    {
        int ixx, iyy;
        ucar.unidata.data.storm.StormADOTInfo sinfo =
            new ucar.unidata.data.storm.StormADOTInfo();
        /* allocate space for data */
       // return new StormADOTInfo.DataGrid(temps, lats, lons, numx, numy);

        areadata_v72 = new StormADOTInfo.DataGrid(temps, lats, lons, numx, numy);

        return 0;
    }




    /**
     * _more_
     *
     * @param indomain _more_
     *
     * @return _more_
     */
    int aodtv72_setdomain(int indomain)
    /* set current ocean domain variable within AODT library memory
       Inputs : domain flag value from input
       Outputs: none
       Return : -81 : error deterimining storm basin
    */
    {
        int   domain;
        float xlon;

        /* obtain current storm center longitude */
        xlon = odtcurrent_v72IR.longitude;
        if ((xlon < -180.0) || (xlon > 180.0)) {
            return -81;
        }

        ixdomain_v72 = indomain;
        /* determine oceanic domain */
        if (indomain == 0) {
            /* automatically determined storm basin */
            if (xlon >= 0.0) {
                domain = 0;  /* atlantic and east pacific to 180W/dateline */
            } else {
                domain = 1;  /* west pacific and other regions */
            }
        } else {
            /* manually determined storm basin */
            domain = indomain - 1;
        }

        /* assign ocean domain flag value to AODT library variable */
        idomain_v72 = domain;

        return 0;
    }


    /**
     * _more_
     *
     * @param ilat _more_
     * @param ilon _more_
     * @param ipos _more_
     *
     * @return _more_
     */
    int aodtv72_setlocation(float ilat, float ilon, int ipos)
    /* set current storm center location within from AODT library memory
       Inputs : AODT library current storm center latitude and longitude values
                and location positioning method : 1-forecast interpolation
                                                  2-laplacian technique
                                                  3-warm spot
                                                  4-extrapolation
       Outputs: none
       Return : -21 : invalid storm center position
                 21 : user selected storm center position
                 22 : auto selected storm center position
    */
    {
        int iret;

        /* assign current storm center latitude value to AODT library variable */
        odtcurrent_v72IR.latitude = ilat;
        /* assign current storm center longitude value to AODT library variable */
        odtcurrent_v72IR.longitude = ilon;
        /* assign current storm center positioning flag to AODT library variable */
        odtcurrent_v72IR.autopos = ipos;
        if ((odtcurrent_v72IR.longitude < -180.)
                || (odtcurrent_v72IR.longitude > 180.)) {
            iret = -21;
        }
        if ((odtcurrent_v72IR.latitude < -90.)
                || (odtcurrent_v72IR.latitude > 90.)) {
            iret = -21;
        }

        iret = 21;  /* user selected image location */
        if (ipos >= 1) {
            iret = 22;
        }


        return iret;
    }



    /**
     * _more_
     *
     * @param datetime _more_
     * @param sat _more_
     *
     * @return _more_
     */
    int aodtv72_setIRimageinfo(double datetime, int sat)
    /* set IR image date/time within AODT library memory
       Inputs : AODT library IR image date/time/satellite information
       Outputs: none
       Return : 0 : o.k.
    */
    {
        /* assign IR image date to AODT library variable */
        odtcurrent_v72IR.date = datetime;

        /* assign IR image satellite type to AODT library variable */
        odtcurrent_v72IR.sattype = sat;

        return 0;
    }






    /**
     * _more_
     *
     * @param idomain _more_
     *
     * @return _more_
     */
    public int aodtv72_calcintensity(int idomain)
    /* Compute intensity values CI, Final T#, and Raw T#.
    Inputs  : global structure odtcurrent_v72 containing current analysis
    Outputs : none
    Return : 71 : storm is over land
              0 : o.k.
    */
    {
        int iok;
        int iret;
        int strength;


        if ((odtcurrent_v72IR.land == 1)) {
            iok  = aodtv72_initcurrent(true, odtcurrent_v72IR);
            iret = 71;
        } else {
            /* calculate current Raw T# value */
            odtcurrent_v72IR.Traw  = aodtv72_Tnoraw(odtcurrent_v72IR,
                    idomain);
            odtcurrent_v72IR.TrawO = odtcurrent_v72IR.Traw;
            /* check for spot analysis or full analysis using history file */
            /* if(hfile_v72==(char *)NULL) { */
            if (true) {
                /* perform spot analysis (only Traw) */
                odtcurrent_v72IR.Tfinal  = odtcurrent_v72IR.Traw;
                odtcurrent_v72IR.Tfinal3 = odtcurrent_v72IR.Traw;
                odtcurrent_v72IR.CI      = odtcurrent_v72IR.Traw;
                odtcurrent_v72IR.CIadjp =
                    aodtv72_latbias(odtcurrent_v72IR.CI,
                                    odtcurrent_v72IR.latitude,
                                    odtcurrent_v72IR.longitude,
                                    odtcurrent_v72IR);
                /* printf("%f %f %f   %f\n",odtcurrent_v72IR.CI,odtcurrent_v72IR.latitude,odtcurrent_v72->IR.longitude,odtcurrent_v72->IR.CIadjp); */
                odtcurrent_v72IR.rule9 = 0;
                /*odtcurrent_v72->IR.TIEraw=aodtv72_TIEmodel();*/
                /*odtcurrent_v72->IR.TIEavg=odtcurrent_v72->IR.TIEraw;*/
                /*odtcurrent_v72->IR.TIEflag=aodtv72_tieflag(); */
            } else {}

            iret = 0;
        }


        return iret;
    }

    /**
     * _more_
     *
     * @param initval _more_
     * @param latitude _more_
     * @param longitude _more_
     * @param odtcurrent_v72IR _more_
     *
     * @return _more_
     */
    float aodtv72_latbias(
            float initval, float latitude, float longitude,
            ucar.unidata.data.storm.StormADOTInfo.IRData odtcurrent_v72IR)
    /* Apply Latitude Bias Adjustment to CI value
        Inputs  : initval  - initial CI value
                  latitude - current latitude of storm
        Outputs : adjusted MSLP value as return value
    */
    {
        float initvalp;

        float initvalpx;
        float value;     /* lat bias adjustement amount (0.00-1.00) */
        int   sceneflag;  /* contains lat bias adjustment flag
                           0=no adjustment
                           1=intermediate adjustment (6 hours)
                           2=full adjustment
                         */

        sceneflag = aodtv72_scenesearch(0);  /* 0 means search for EIR based parameters... cdo, etc */
        value = 1.0f;  /* this value should be return from scenesearch() */
        /* printf("sceneflag=%d  value=%f\n",sceneflag,value); */
        odtcurrent_v72IR.LBflag = sceneflag;
        /* initvalp=aodtv72_getpwval(0,initval); TLO */
        initvalp = 0.0f;
        if (sceneflag >= 2) {
            /* EIR scene */
            if ((latitude >= 0.0)
                    && ((longitude >= -100.0) && (longitude <= -40.0))) {
                /* do not make adjustment in N Indian Ocean */
                return initvalp;
            }
            /* apply bias adjustment to pressure */
            /* initvalp=-1.0*value*(-20.60822+(0.88463*A_ABS(latitude)));  */
            initvalp = value * (7.325f - (0.302f * Math.abs(latitude)));
        }

        return initvalp;
    }

    /**
     * _more_
     *
     * @param type _more_
     *
     * @return _more_
     */
    int aodtv72_scenesearch(int type) {
        int    curflag = 1, flag, eirflag;
        float  eirvalue, civalue, ciadjvalue, latitude;
        double curtime, xtime, curtimem6, mergetimefirst, mergetimelast,
               firsttime = -9999.0;

        float  pvalue;

        /* if(((odthistoryfirst_v72==0)&&(ostartstr_v72==TRUE))&&(hfile_v72!=(char *)NULL)) { */

        if (true) {
            flag   = 2;
            pvalue = 1.0f;
            return flag;
        }

        return flag;
    }


    /**
     * _more_
     *
     * @param redo _more_
     * @param odtcurrent_v72IR _more_
     *
     * @return _more_
     */
    int aodtv72_initcurrent(boolean redo,
                            StormADOTInfo.IRData odtcurrent_v72IR)
    /* initialize odtcurrent_v72 array or reset values for land interaction situations */
    {
        int b, bb;
        // char comm[50]="\0";

        if ( !redo) {
            //odtcurrent_v72=(struct odtdata *)malloc(sizeof(struct odtdata));
            odtcurrent_v72IR.latitude  = 999.99f;
            odtcurrent_v72IR.longitude = 999.99f;
            odtcurrent_v72IR.land      = 0;
            odtcurrent_v72IR.autopos   = 0;
            //strcpy(odtcurrent_v72IR.comment,comm);
            //diagnostics_v72=(char *)calloc((size_t)50000,sizeof(char));
            //hfile_v72=(char *)calloc((size_t)200,sizeof(char));
            //fixfile_v72=(char *)calloc((size_t)200,sizeof(char));

            //b=sizeof(float);
            //bb=sizeof(double);
            //fcstlat_v72=(float *)calloc((size_t)5,b);
            //fcstlon_v72=(float *)calloc((size_t)5,b);
            //fcsttime_v72=(double *)calloc((size_t)5,bb);
        }

        odtcurrent_v72IR.Traw          = 0.0f;
        odtcurrent_v72IR.TrawO         = 0.0f;
        odtcurrent_v72IR.Tfinal        = 0.0f;
        odtcurrent_v72IR.Tfinal3       = 0.0f;
        odtcurrent_v72IR.CI            = 0.0f;
        odtcurrent_v72IR.eyet          = 99.99f;
        odtcurrent_v72IR.warmt         = 99.99f;
        odtcurrent_v72IR.cloudt        = 99.99f;
        odtcurrent_v72IR.cloudt2       = 99.99f;
        odtcurrent_v72IR.cwcloudt      = 99.99f;
        odtcurrent_v72IR.warmlatitude  = 999.99f;
        odtcurrent_v72IR.warmlongitude = 999.99f;
        odtcurrent_v72IR.eyecdosize    = 0.0f;
        odtcurrent_v72IR.eyestdv       = 0.0f;
        odtcurrent_v72IR.cloudsymave   = 0.0f;
        odtcurrent_v72IR.eyescene      = 0;
        odtcurrent_v72IR.cloudscene    = 0;
        odtcurrent_v72IR.eyesceneold   = -1;
        odtcurrent_v72IR.cloudsceneold = -1;
        odtcurrent_v72IR.rule9         = 0;
        odtcurrent_v72IR.rule8         = 0;
        odtcurrent_v72IR.LBflag        = 0;
        odtcurrent_v72IR.rapiddiss     = 0;
        odtcurrent_v72IR.eyefft        = 0;
        odtcurrent_v72IR.cloudfft      = 0;
        odtcurrent_v72IR.cwring        = 0;
        odtcurrent_v72IR.ringcb        = 0;
        odtcurrent_v72IR.ringcbval     = 0;
        odtcurrent_v72IR.ringcbvalmax  = 0;
        odtcurrent_v72IR.CIadjp        = 0.0f;
        odtcurrent_v72IR.rmw           = -99.9f;
        /*odtcurrent_v72->IR.TIEflag=0;*/
        /*odtcurrent_v72->IR.TIEraw=0.0;*/
        /*odtcurrent_v72->IR.TIEavg=0.0;*/
        /*odtcurrent_v72->IR.sst=-99.9;*/
        //if(!redo) odtcurrent_v72->nextrec=NULL;  /* added by CDB */

        return 0;
    }

    /**
     * Compute initial Raw T-Number value using original Dvorak rules
     * @param odtcurrent
     * @param idomain_v72
     * @return return value is Raw T#
     */

    float aodtv72_Tnoraw(
            ucar.unidata.data.storm.StormADOTInfo.IRData odtcurrent,
            int idomain_v72)
    /* Compute initial Raw T-Number value using original Dvorak rules
        Inputs  : global structure odtcurrent_v72 containing current analysis
        Outputs : return value is Raw T#

                           ODT SCENE/TEMPERATURE TABLE
        BD   | WMG   OW    DG    MG    LG     B     W    CMG   CDG |
        TEMP |30.0   0.0 -30.0 -42.0 -54.0 -64.0 -70.0 -76.0 -80.0+|
    ---------------------------------------------------------------|
    Atl EYE  | 3.5   4.0   4.5   4.5   5.0   5.5   6.0   6.5   7.0 |
        EMBC | 3.5   3.5   4.0   4.0   4.5   4.5   5.0   5.0   5.0 |
        CDO  | 3.0   3.0   3.5   4.0   4.5   4.5   4.5   5.0   5.0 |
    ---------------------------------------------------------------|
    Pac EYE  | 4.0   4.0   4.0   4.5   4.5   5.0   5.5   6.0   6.5 |
        EMBC | 3.5   3.5   4.0   4.0   4.5   4.5   5.0   5.0   5.0 |
        CDO  | 3.0   3.5   3.5   4.0   4.5   4.5   4.5   4.5   5.0 |
    ---------------------------------------------------------------|
    Cat diff |  0     1     2     3     4     5     6     7     8  |
        add  | 0.0   0.0   0.0   0.0   0.0-->0.5   0.5-->1.0   1.5 | (old)
        add  |-0.5  -0.5   0.0   0.0-->0.5   0.5   0.5-->1.0   1.0 | (new)
    ---------------------------------------------------------------|
    */
    {

        double [][] eno = {
            {
                1.00, 2.00, 3.25, 4.00, 4.75, 5.50, 5.90, 6.50, 7.00, 7.50,
                8.00
            },  /* original plus adjusted > CDG+ */
            {
                1.50, 2.25, 3.30, 3.85, 4.50, 5.00, 5.40, 5.75, 6.25, 6.50,
                7.00
            }
        };  /* adjusted based     */
        double [][] cdo = {
            {
                2.00, 2.40, 3.25, 3.50, 3.75, 4.00, 4.10, 4.20, 4.30, 4.40,
                4.70
            }, {
                2.05, 2.40, 3.00, 3.20, 3.40, 3.55, 3.65, 3.75, 3.80, 3.90,
                4.10
            }
        };
        double [] curbnd    = {
            1.0, 1.5, 2.5, 3.0, 3.5, 4.0, 4.5
        };
        double [] shrdst    = {
            0.0, 35.0, 50.0, 80.0, 110.0, 140.0
        };
        double [] shrcat    = {
            3.5, 3.0, 2.5, 2.25, 2.0, 1.5
        };

        double [][] diffchk = {
            {
                0.0, 0.5, 1.2, 1.7, 2.2, 2.7, 0.0, 0.0, 0.1, 0.5
            },  /* shear scene types... original Rule 8 rules */
            {
                0.0, 0.5, 1.7, 2.2, 2.7, 3.2, 0.0, 0.0, 0.1, 0.5
            },  /*   eye scene types... add 0.5 to Rule 8 rules */
            {
                0.0, 0.5, 0.7, 1.2, 1.7, 2.2, 0.0, 0.0, 0.1, 0.5
            }
        };  /* other scene types... subtract 0.5 from Rule 8 rules */
        double eyeadjfacEYE[] = { 0.011, 0.015 };  /* modified wpac value to be closer to atlantic */
        double symadjfacEYE[]    = { -0.015, -0.015 };
        double dgraysizefacCLD[] = { 0.002, 0.001 };
        double symadjfacCLD[]    = { -0.030, -0.015 };

        int    diffchkcat;
        int    ixx, cloudcat, eyecat, diffcat, rp, xrp, rb;
        float  incval, lastci, lasttno, lastr9, lastraw;
        float xpart, xparteye, xaddtno, eyeadj, spart, ddvor, dvorchart,
              ciadj;
        float sdist, cloudtemp, eyetemp, fftcloud;
        float t1val, t6val, t12val, t18val, t24val, delt1, delt6, delt12,
              delt18, delt24;
        float                t1valraw, t1valrawx, txvalmin, txvalmax;
        double               curtime, xtime, firsttime, firstlandtime;
        double ttime1, ttime6, ttime12, ttime18, ttime24, t1valrawxtime;
        StormADOTInfo.IRData odthistory, prevrec;
        boolean              oceancheck, adjustshear, firstland;
        boolean              t1found   = false,
                             t6found   = false,
                             t12found  = false,
                             t18found  = false,
                             t24found  = false;
        boolean              first6hrs = false;
        float                symadj, dgraysizeadj, deltaT;


        cloudtemp = odtcurrent.cloudt;
        eyetemp   = odtcurrent.eyet;
        cloudcat  = 0;
        eyecat    = 0;
        lastci    = 4.0f;
        xpart     = 0.0f;

        for (ixx = 0; ixx < 10; ixx++) {
            /* compute cloud category */
            if ((cloudtemp <= ucar.unidata.data.storm.StormADOTInfo
                    .ebd_v72[ixx]) && (cloudtemp > ucar.unidata.data.storm
                    .StormADOTInfo.ebd_v72[ixx + 1])) {
                cloudcat = ixx;
                xpart = (float) (cloudtemp
                        - ucar.unidata.data.storm.StormADOTInfo
                            .ebd_v72[cloudcat]) / (float) (StormADOTInfo
                                .ebd_v72[cloudcat + 1] - ucar.unidata.data
                                    .storm.StormADOTInfo.ebd_v72[cloudcat]);
            }
            /* compute eye category for eye adjustment */
            if ((eyetemp <= ucar.unidata.data.storm.StormADOTInfo
                    .ebd_v72[ixx]) && (eyetemp > ucar.unidata.data.storm
                    .StormADOTInfo.ebd_v72[ixx + 1])) {
                eyecat = ixx;
            }
            /* eyetemp=Math.min(0.0,eyetemp); */
        }
        if (odtcurrent.eyescene == 1) {
            /* for pinhole eye, determine what storm should be seeing */
            /* eyetemp=pinhole(odtcurrent_v72->IR.latitude,odtcurrent_v72->IR.longitude,eyetemp); */
            /*eyetemp=(9.0-eyetemp)/2.0;  / this matches DT used at NHC (jack beven) */
            eyetemp = (float) (eyetemp - 9.0) / 2.0f;  /* between +9C (beven) and measured eye temp (turk) */
            odtcurrent.eyet = eyetemp;
        }

        /* category difference between eye and cloud region */
        diffcat = Math.max(0, cloudcat - eyecat);

        /* if scenetype is EYE */
        rp       = odtcurrent.ringcbval;
        rb       = odtcurrent.ringcb;
        fftcloud = odtcurrent.cloudfft;

        if (odtcurrent.cloudscene == 3) {
            /* CURVED BAND */
            rp     = Math.min(30, rp + 1);  /* added 1 for testing */
            xrp    = rp / 5;
            incval = 0.1f;
            if (xrp == 1) {
                incval = 0.2f;
            }
            ddvor   = (float) curbnd[xrp];
            xaddtno = incval * (float) (rp - (xrp * 5));
            /* printf("rp=%d  xrp=%d  rb=%d  ddvor=%f  xaddtno=%f\n",rp,xrp,rb,ddvor,xaddtno); */
            ddvor = ddvor + xaddtno;
            if (rb == 5) {
                ddvor = Math.min(4.0f, ddvor + 0.5f);
            }
            if (rb == 6) {
                ddvor = Math.min(4.5f, ddvor + 1.0f);
            }
            diffchkcat = 2;  /* added for test - non-eye/shear cases */
        } else if (odtcurrent.cloudscene == 4) {
            /* POSSIBLE SHEAR -- new definition from NHC */
            ixx   = 0;
            ddvor = 1.0f;
            sdist = odtcurrent.eyecdosize;  /* shear distance */
            while (ixx < 5) {
                if ((sdist >= shrdst[ixx]) && (sdist < shrdst[ixx + 1])) {
                    spart = (float) ((sdist - shrdst[ixx])
                                     / (shrdst[ixx + 1] - shrdst[ixx]));
                    xaddtno = (float) ((spart
                                        * (shrcat[ixx + 1] - shrcat[ixx])));
                    ddvor = (float) (shrcat[ixx] + xaddtno);
                    ixx   = 5;
                } else {
                    ixx++;
                }
            }
            diffchkcat = 0;  /* added for test - shear cases */
        } else {
            /* EYE or NO EYE */
            if (odtcurrent.eyescene <= 2) {
                /* EYE */
                xaddtno = (float) (xpart
                                   * (eno[idomain_v72][cloudcat + 1]
                                      - eno[idomain_v72][cloudcat]));
                /* cloud category must be white (-70C) or below for full adjustment;
                 value will be merged in starting at black (-64C) /
                if(cloudcat<5) {         / gray shades /
                xparteye=0.00;
                } else if(cloudcat==5) { / black /
                xparteye=xpart;
                } else {                 / white and colder /
                xparteye=1.00;
                } */
                eyeadj = (float) eyeadjfacEYE[idomain_v72]
                         * (eyetemp - cloudtemp);
                /* symadj=-0.02*(odtcurrent_v72->IR.cloudsymave);  */
                symadj = (float) symadjfacEYE[idomain_v72]
                         * (odtcurrent.cloudsymave);
                /* printf("EYE : cloudsymave=%f  symadj=%f\n",odtcurrent_v72->IR.cloudsymave,symadj); */
                ddvor = (float) eno[idomain_v72][cloudcat] + xaddtno + eyeadj
                        + symadj;
                /* printf("EYE : xaddtno=%f  eyeadj=%f  symadj=%f   ddvor=%f\n",xaddtno,eyeadj,symadj,ddvor);  */
                ddvor = Math.min(ddvor, 9.0f);
                /* printf("ddvor=%f\n",ddvor); */
                if (odtcurrent.eyescene == 2) {
                    ddvor = Math.min(ddvor - 0.5f, 6.5f);  /* LARGE EYE adjustment */
                }
                /* if(odtcurrent_v72->IR.eyescene==3)  ddvor=Math.min(ddvor-0.5,6.0);     / LARGE RAGGED EYE adjustment */
                diffchkcat = 1;  /* added for test - eye cases */
                /* printf("ddvor=%f\n",ddvor); */
            } else {
                /* NO EYE */
                /* CDO */
                xaddtno = (float) (xpart
                                   * (cdo[idomain_v72][cloudcat + 1]
                                      - cdo[idomain_v72][cloudcat]));
                /* dgraysizeadj=0.002*odtcurrent_v72->IR.eyecdosize; */
                dgraysizeadj = (float) dgraysizefacCLD[idomain_v72]
                               * odtcurrent.eyecdosize;
                /* printf("CDO : dgraysize=%f  symadj=%f\n",odtcurrent_v72->IR.eyecdosize,dgraysizeadj); */
                /* symadj=-0.03*(odtcurrent_v72->IR.cloudsymave); */
                symadj = (float) symadjfacCLD[idomain_v72]
                         * (odtcurrent.cloudsymave);
                /* printf("CDO : cloudsymave=%f  symadj=%f\n",odtcurrent_v72->IR.cloudsymave,symadj); */
                ddvor = (float) cdo[idomain_v72][cloudcat] + xaddtno
                        + dgraysizeadj + symadj;
                ddvor = ddvor - 0.1f;  /* bias adjustment */
                /* printf("CDO : xaddtno=%f dgraysizeadj=%f  symadj=%f   ddvor=%f\n",xaddtno,dgraysizeadj,symadj,ddvor); */
                ciadj = 0.0f;
                if (odtcurrent.cloudscene == 0) {  /* CDO */
                    if (lastci >= 4.5) {
                        ciadj = Math.max(0.0f, Math.min(1.0f, lastci - 4.5f));
                    }
                    if (lastci <= 3.0) {
                        ciadj = Math.min(0.0f,
                                         Math.max(-1.0f, lastci - 3.0f));
                    }
                    /* printf("CDO : lastci=%f   xaddtno=%f\n",lastci,ciadj); */
                    ddvor = ddvor + ciadj;
                }
                if (odtcurrent.cloudscene == 1) {  /* EMBEDDED CENTER */
                    ciadj = Math.max(0.0f, Math.min(1.5f, lastci - 4.0f));
                    /* printf("EMBC : lastci=%f   xaddtno=%f\n",lastci,ciadj); */
                    ddvor = ddvor + ciadj;         /* changed from 0.5 */
                }
                if (odtcurrent.cloudscene == 2) {  /* IRREGULAR CDO (PT=3.5) */
                    ddvor = ddvor + 0.3f;  /* additional IrrCDO bias adjustment */
                    ddvor = Math.min(3.5f, Math.max(2.5f, ddvor));
                }
                diffchkcat = 2;  /* added for test - non-eye/shear cases */
            }
        }

        dvorchart = ((float) (int) (ddvor * 10.0f)) / 10.0f;
        //odtcurrent_v72IR.TrawO=dvorchart;


        return dvorchart;

    }




}

