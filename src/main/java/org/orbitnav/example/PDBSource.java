package org.orbitnav.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Bond;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.FileParsingParameters;
import org.biojava.bio.structure.io.PDBFileReader;

public final class PDBSource {
    
    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public PDBSource(File pdbFile) {
        FileParsingParameters fpp = new FileParsingParameters();
        fpp.setCreateAtomBonds(true);
        PDBFileReader pdbFileReader = new PDBFileReader();
        pdbFileReader.setFileParsingParameters(fpp);
        try {
            structure = pdbFileReader.getStructure(pdbFile);
        } catch (IOException e) {
            structure = null;
        }
    }
    
    public List<VizAtom> getAtoms(double radius) {
        ArrayList<VizAtom> atomList = new ArrayList<VizAtom>();
        if (structure != null) {
            for (Chain chain : structure.getChains()) {
                for (Group group : chain.getAtomGroups()) {
                    for (Atom atom : group.getAtoms()) {
                        atomList.add(new VizAtom(atom, radius));
                    }
                }
            }
        }
        return atomList;
    }
    
    public List<VizBond> getBonds(double radius) {
        Map<Atom, Bond> atomMap = new HashMap<Atom, Bond>();
        ArrayList<VizBond> bondList = new ArrayList<VizBond>();
        if (structure != null) {
            for (Chain chain : structure.getChains()) {
                for (Group group : chain.getAtomGroups()) {
                    for (Atom atom : group.getAtoms()) {
                        for (Bond bond : atom.getBonds()) {
                            Atom atomA = bond.getAtomA();
                            Atom atomB = bond.getAtomB();
                            if (atomMap.containsKey(atomA) || atomMap.containsKey(atomB)) {
                                // bond already present
                            } else {
                                atomMap.put(atomA, bond);
                            }
                        }
                    }
                }
            }
        }
        for (Bond bond : atomMap.values()) {
            bondList.add(new VizBond(bond, radius));
        }
        return bondList;
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE

    private Structure structure;
    
}
