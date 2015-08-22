/*
 * ScrabbleTransferable.java
 *
 * Created on September 2, 2002, 8:10 PM
 */

/**
 * 
 * @author lwe
 * @version
 */

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ScrabbleTransferable implements Transferable {

    private String           transferData;
    public static DataFlavor tileFlavor = DataFlavor.stringFlavor;

    public ScrabbleTransferable(String transferData) {
        this.transferData = transferData;
    }

    /**
     * Returns an object which represents the data to be transferred. The class
     * of the object returned is defined by the representation class of the
     * flavor.
     * 
     * @param flavor
     *            the requested flavor for the data
     * @see DataFlavor#getRepresentationClass
     * @exception IOException
     *                if the data is no longer available in the requested
     *                flavor.
     * @exception UnsupportedFlavorException
     *                if the requested data flavor is not supported.
     */
    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        if (flavor.equals(tileFlavor)) {
            return transferData;
        }
        else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    /**
     * Returns whether or not the specified data flavor is supported for this
     * object.
     * 
     * @param flavor
     *            the requested flavor for the data
     * @return boolean indicating wjether or not the data flavor is supported
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(tileFlavor);
    }

    /**
     * Returns an array of DataFlavor objects indicating the flavors the data
     * can be provided in. The array should be ordered according to preference
     * for providing the data (from most richly descriptive to least
     * descriptive).
     * 
     * @return an array of data flavors in which this data can be transferred
     */
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { tileFlavor };
    }
}