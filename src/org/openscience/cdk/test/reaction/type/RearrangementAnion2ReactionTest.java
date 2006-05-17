package org.openscience.cdk.test.reaction.type;


import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.ISetOfMolecules;
import org.openscience.cdk.interfaces.ISetOfReactions;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainerCreator;
import org.openscience.cdk.reaction.IReactionProcess;
import org.openscience.cdk.reaction.type.RearrangementAnion2Reaction;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.test.CDKTestCase;
import org.openscience.cdk.tools.HydrogenAdder;
import org.openscience.cdk.tools.LonePairElectronChecker;
import org.openscience.cdk.tools.manipulator.ReactionManipulator;
/**
 * <p>IReactionProcess which participate in movement resonance. 
 * This reaction could be represented as [A-]-B=C => A=B-[C-]. Due to 
 * the negative charge of the atom A, the double bond in position 2 is 
 * desplaced.</p>
 * <pre>
 *  ISetOfMolecules setOfReactants = DefaultChemObjectBuilder.getInstance().newSetOfMolecules();
 *  setOfReactants.addMolecule(new Molecule());
 *  IReactionProcess type = new RearrangementAnion1Reaction();
 *  Object[] params = {Boolean.FALSE};
    type.setParameters(params);
 *  ISetOfReactions setOfReactions = type.initiate(setOfReactants, null);
 *  </pre>
 * 
 * <p>We have the possibility to localize the reactive center. Good method if you
 * want to localize the reaction in a fixed point</p>
 * <pre>atoms[0].setFlag(CDKConstants.REACTIVE_CENTER,true);</pre>
 * <p>Moreover you must put the parameter Boolean.TRUE</p>
 * <p>If the reactive center is not localized then the reaction process will
 * try to find automatically the posible reactive center.</p>
 * 
 * 
 * @author         Miguel Rojas
 * 
 * @cdk.created    2006-05-05
 * @cdk.module     reaction
 * @cdk.set        reaction-types
 * 
 **/
/**
 * TestSuite that runs a test for the RearrangementAnion2ReactionTest.
 * Generalized Reaction: [A-]-B=C => A=B-[C-].
 *
 * @cdk.module test-reaction
 */
 
public class RearrangementAnion2ReactionTest extends CDKTestCase {
	
	private IReactionProcess type;
	
	/**
	 * Constructror of the RearrangementAnion2ReactionTest object
	 *
	 */
	public  RearrangementAnion2ReactionTest() {
		type  = new RearrangementAnion2Reaction();
	}
    
	public static Test suite() {
		return new TestSuite(RearrangementAnion2ReactionTest.class);
	}
	/**
	 * A unit test suite for JUnit. Reaction: [C-]-C=C-C => C=C-[C-]-C
	 * Automatic sarch of the centre active.
	 *
	 * @return    The test suite
	 */
	public void testAutomaticSearchCentreActiveExample1() throws ClassNotFoundException, CDKException, java.lang.Exception {
		
		/* [C-]-C=C-C */
		IMolecule molecule = getMolecule1();
		
		ISetOfMolecules setOfReactants = DefaultChemObjectBuilder.getInstance().newSetOfMolecules();
		setOfReactants.addMolecule(molecule);
		
        Object[] params = {Boolean.FALSE};
        type.setParameters(params);
        ISetOfReactions setOfReactions = type.initiate(setOfReactants, null);
        
        Assert.assertEquals(1, setOfReactions.getReactionCount());
        Assert.assertEquals(1, setOfReactions.getReaction(0).getProductCount());

        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        Assert.assertEquals(-1, product.getAtomAt(2).getFormalCharge());
        Assert.assertEquals(0, product.getLonePairCount(molecule.getAtomAt(1)));
        
        /*C=C-[C-]-C*/
        IMolecule molecule2 = getMolecule2();
        
        QueryAtomContainer qAC = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product);
		Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule2,qAC));
        
        Assert.assertEquals(5,setOfReactions.getReaction(0).getMappings().length);
        
        IAtom mappedProduct = (IAtom)ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtomAt(0));
        assertEquals(mappedProduct, product.getAtomAt(0));
	}

	/**
	 * A unit test suite for JUnit. Reaction: [C-]-C=C-C => C=C-[C-]-C
	 * Manually put of the centre active.
	 *
	 * @return    The test suite
	 */
	public void testManuallyPutCentreActiveExample1() throws ClassNotFoundException, CDKException, java.lang.Exception {
		ISetOfMolecules setOfReactants = DefaultChemObjectBuilder.getInstance().newSetOfMolecules();
		/*[C-]-C=C-C */
		IMolecule molecule = getMolecule1();
		setOfReactants.addMolecule(molecule);
		
		/*manually put the centre active*/
		molecule.getAtomAt(0).setFlag(CDKConstants.REACTIVE_CENTER,true);
		molecule.getBondAt(0).setFlag(CDKConstants.REACTIVE_CENTER,true);
		molecule.getAtomAt(1).setFlag(CDKConstants.REACTIVE_CENTER,true);
		molecule.getBondAt(1).setFlag(CDKConstants.REACTIVE_CENTER,true);
		molecule.getAtomAt(2).setFlag(CDKConstants.REACTIVE_CENTER,true);
		molecule.getBondAt(2).setFlag(CDKConstants.REACTIVE_CENTER,true);
		molecule.getAtomAt(3).setFlag(CDKConstants.REACTIVE_CENTER,true);
		
        Object[] params = {Boolean.TRUE};
        type.setParameters(params);
        
        /* iniciate */
        ISetOfReactions setOfReactions = type.initiate(setOfReactants, null);
        
        Assert.assertEquals(1, setOfReactions.getReactionCount());
        Assert.assertEquals(1, setOfReactions.getReaction(0).getProductCount());

        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        
        /*C=C-[C-]-C*/
        IMolecule molecule2 = getMolecule2();
        
        QueryAtomContainer qAC = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product);
		Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule2,qAC));
        
        		
	}
	/**
	 * A unit test suite for JUnit. Reaction: [C-]-C=C-C => C=C-[C-]-C
	 * Test of mapped between the reactant and product. Only is mapped the centre active.
	 *
	 * @return    The test suite
	 */
	public void testMappingExample1() throws ClassNotFoundException, CDKException, java.lang.Exception {
		ISetOfMolecules setOfReactants = DefaultChemObjectBuilder.getInstance().newSetOfMolecules();
		/*[C-]-C=C-C*/
		IMolecule molecule = getMolecule1();
		setOfReactants.addMolecule(molecule);
		
		/*automatic search of the centre active*/
        Object[] params = {Boolean.FALSE};
        type.setParameters(params);
        
        /* iniciate */
        ISetOfReactions setOfReactions = type.initiate(setOfReactants, null);
        
        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);

        Assert.assertEquals(5,setOfReactions.getReaction(0).getMappings().length);
        IAtom mappedProductA1 = (IAtom)ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtomAt(0));
        assertEquals(mappedProductA1, product.getAtomAt(0));
        mappedProductA1 = (IAtom)ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtomAt(1));
        assertEquals(mappedProductA1, product.getAtomAt(1));
        mappedProductA1 = (IAtom)ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtomAt(2));
        assertEquals(mappedProductA1, product.getAtomAt(2));
        
	}
	/**
	 * A unit test suite for JUnit. Reaction: [F+]=C1-[C-]-C=C-C=C1 => [F+]=C1-[C=]-C-[C-]-C=C1
	 * Automatic sarch of the centre active.
	 *
	 * @return    The test suite
	 */
	public void testAutomaticSearchCentreActiveExample3() throws ClassNotFoundException, CDKException, java.lang.Exception {
		
		/* [F+]=C1-[C-]-C=C-C=C1*/
		Molecule molecule = (new SmilesParser()).parseSmiles("[F+]=C1C=CC=C[C-]1");
	    HydrogenAdder adder = new HydrogenAdder();
        adder.addImplicitHydrogensToSatisfyValency(molecule);
        LonePairElectronChecker lpcheck = new LonePairElectronChecker();
        lpcheck.newSaturate(molecule);
		
		ISetOfMolecules setOfReactants = DefaultChemObjectBuilder.getInstance().newSetOfMolecules();
		setOfReactants.addMolecule(molecule);
		
        Object[] params = {Boolean.FALSE};
        type.setParameters(params);
        ISetOfReactions setOfReactions = type.initiate(setOfReactants, null);
        
        Assert.assertEquals(1, setOfReactions.getReactionCount());
        Assert.assertEquals(1, setOfReactions.getReaction(0).getProductCount());

        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        
        /*[F+]=C1-[C=]-C-[C-]-C=C1*/
        Molecule molecule2 = (new SmilesParser()).parseSmiles("[F+]=C1-C=C-[C-]-C=C1");
        adder.addImplicitHydrogensToSatisfyValency(molecule2);
        lpcheck.newSaturate(molecule2);
        
        QueryAtomContainer qAC = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product);
		Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule2,qAC));
        
        Assert.assertEquals(5,setOfReactions.getReaction(0).getMappings().length);
	}
	/**
	 * get the molecule 1: [C-]-C=C-C
	 * 
	 * @return The IMolecule
	 */
	private IMolecule getMolecule1()throws ClassNotFoundException, CDKException, java.lang.Exception {
		Molecule molecule = (new SmilesParser()).parseSmiles("[C-]-C=C-C");
	    HydrogenAdder adder = new HydrogenAdder();
        adder.addImplicitHydrogensToSatisfyValency(molecule);
        return molecule;
	}
	/**
	 * get the molecule 2: C=C-[C-]-C
	 * 
	 * @return The IMolecule
	 */
	private IMolecule getMolecule2()throws ClassNotFoundException, CDKException, java.lang.Exception {
		Molecule molecule = (new SmilesParser()).parseSmiles("C=C-[C-]-C");
		HydrogenAdder adder = new HydrogenAdder();
        adder.addImplicitHydrogensToSatisfyValency(molecule);
        return molecule;
	}
}
