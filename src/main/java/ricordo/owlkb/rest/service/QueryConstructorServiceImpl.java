package ricordo.owlkb.rest.service;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.io.File;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: sarala
 * Date: 14/03/12
 * Time: 10:29
 * To change this template use File | Settings | File Templates.
 */
public class QueryConstructorServiceImpl implements QueryConstructorService{

    private OWLOntologyManager owlOntologyManager = null;
    private OWLOntology queryOntology = null;
    private IRI docIRI = null;

    public QueryConstructorServiceImpl(IRI docIRI, OWLOntologyManager owlOntologyManager) {
        this.owlOntologyManager = owlOntologyManager;
        this.docIRI = docIRI;
        try {
            queryOntology = owlOntologyManager.loadOntology(docIRI);
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run a query constructed in manchester query syntax to create an OWL class expression
     * @param manchesterQuery
     * @return OWLClassExpression
     */
    public OWLClassExpression runManchesterQuery(String manchesterQuery){
        ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(owlOntologyManager.getOWLDataFactory(), manchesterQuery);
        parser.setDefaultOntology(this.queryOntology);
        Set<OWLOntology> importsClosure = queryOntology.getImportsClosure();
        BidirectionalShortFormProvider bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(owlOntologyManager, importsClosure, new SimpleShortFormProvider());
        OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
        parser.setOWLEntityChecker(entityChecker);

        OWLClassExpression classExp=null;
        try {
            classExp = parser.parseClassExpression();
        } catch (ParserException e) {
            e.printStackTrace();
        }
		parser = null;
		importsClosure = null;
		bidiShortFormProvider = null;
		entityChecker = null;
		
        return classExp;
    }

    /**
     * Add axioms to a OWL file
     * @param axiomSet
     */
    public void addAxioms(Set<OWLAxiom> axiomSet) {
        owlOntologyManager.addAxioms(queryOntology, axiomSet);
        try {
            owlOntologyManager.saveOntology(queryOntology, docIRI);
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete axioms from a OWL file
     * @param axiomSet
     */
    public void deleteAxioms(Set<OWLAxiom> axiomSet) {
        owlOntologyManager.removeAxioms(queryOntology, axiomSet);
        try {
            owlOntologyManager.saveOntology(queryOntology, docIRI);
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }
    }
}
