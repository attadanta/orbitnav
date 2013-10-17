package org.arcball.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.PDBFileReader;

public final class PDBSource {
    
    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public PDBSource(File pdbFile) {
        PDBFileReader pdbFileReader = new PDBFileReader();
        try {
            structure = pdbFileReader.getStructure(pdbFile);
        } catch (IOException e) {
            structure = null;
        }
    }
    
    public List<VizAtom> getAtoms(double radius) {
        ArrayList<VizAtom> atomList = new ArrayList<>();
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
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE

    private Structure structure;
    
}
