package br.com.munif.espwebmavenplugin;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.PieChart;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 *
 * @author munif
 */
@Mojo(name = "jsfcrud", requiresDependencyResolution = ResolutionScope.RUNTIME)
public class JsfCrud extends AbstractMojo {

    @Parameter(property = "project", required = true, readonly = true)
    private MavenProject project;

    @Parameter(property = "entidade")
    private String entidade;

    private Class entidadeClass;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            PluginsUtil.assina();
            System.out.println("---------->" + project.getName() + " v:" + project.getVersion());
            System.out.println("-----> Entidade" + entidade);
            entidadeClass = PluginsUtil.getClassLoader(project).loadClass(entidade);
            geraControlador();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JsfCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void geraControlador() {
        String nomePacote = "controladores";
        String pastaControlador = PluginsUtil.windowsSafe(project.getCompileSourceRoots().get(0)) + "/".concat(nomePacote.replaceAll("\\.", "/"));
        File f = new File(pastaControlador);
        f.mkdirs();
        File arquivoControlador = new File(pastaControlador + "/" + entidadeClass.getSimpleName() + "ControladorJSF.java");

        try {
            FileWriter fw = new FileWriter(arquivoControlador);
            fw.write(""
                    + "package "+nomePacote+";\n"
                    + "\n"
                    + "import "+entidade+";\n"
                    + "import br.com.munif.util.ConverterGenerico;\n"
                    + "import br.com.munif.bereja.negocio.*;\n"
                    + "import br.com.munif.util.FacesUtil;\n"
                    + "import br.com.munif.util.RevisaoEObjeto;\n"
                    + "import java.io.Serializable;\n"
                    + "import java.util.List;\n"
                    + "import javax.faces.bean.ManagedBean;\n"
                    + "import javax.faces.bean.SessionScoped;\n"
                    + "import javax.faces.convert.Converter;\n"
                    + "\n"
                    + "/**\n"
                    + " *\n"
                    + " * @author Gerador EspeWeb em "+new Date()+"\n"
                    + " */\n"
                    + "@SessionScoped\n"
                    + "@ManagedBean\n"
                    + "public class "+entidadeClass.getSimpleName()+"ControladorJSF implements Serializable {\n"
                    + "\n"
                    + "    private final Service<"+entidadeClass.getSimpleName()+"> service;\n"
                    + "\n"
                    + "    private "+entidadeClass.getSimpleName()+" entidade;\n"
                    + "    private List<"+entidadeClass.getSimpleName()+"> lista;\n"
                    + "    private List<RevisaoEObjeto> listaRevisao;\n"
                    + "    private String filtro;\n"
                    + "    private Boolean novo;\n"
                    + "\n"
                    + "    public List<RevisaoEObjeto> getListaRevisao() {\n"
                    + "        return listaRevisao;\n"
                    + "    }\n"
                    + "\n"
                    + "    public Boolean getNovo() {\n"
                    + "        return novo;\n"
                    + "    }\n"
                    + "\n"
                    + "    public "+entidadeClass.getSimpleName()+"ControladorJSF() {\n"
                    + "        this.service = new Service<>("+entidadeClass.getSimpleName()+".class);\n"
                    + "    }\n"
                    + "\n"
                    + "    public String getFiltro() {\n"
                    + "        return filtro;\n"
                    + "    }\n"
                    + "\n"
                    + "    public void setFiltro(String filtro) {\n"
                    + "        this.filtro = filtro;\n"
                    + "    }\n"
                    + "\n"
                    + "    public "+entidadeClass.getSimpleName()+" getEntidade() {\n"
                    + "        return entidade;\n"
                    + "    }\n"
                    + "\n"
                    + "    public void setEntidade("+entidadeClass.getSimpleName()+" entidade) {\n"
                    + "        this.entidade = entidade;\n"
                    + "        novo = false;\n"
                    + "        listaRevisao = service.listaVersoes(entidade.getId());\n"
                    + "    }\n"
                    + "\n"
                    + "    public List<"+entidadeClass.getSimpleName()+"> getLista() {\n"
                    + "        if (lista == null) {\n"
                    + "            lista = service.lista();\n"
                    + "        }\n"
                    + "        return lista;\n"
                    + "    }\n"
                    + "\n"
                    + "    public void setLista(List<"+entidadeClass.getSimpleName()+"> lista) {\n"
                    + "        this.lista = lista;\n"
                    + "    }\n"
                    + "\n"
                    + "    public void novo() {\n"
                    + "        entidade = new "+entidadeClass.getSimpleName()+"();\n"
                    + "        novo = true;\n"
                    + "    }\n"
                    + "\n"
                    + "    public void excluir("+entidadeClass.getSimpleName()+" aRemover) {\n"
                    + "        service.excluir(aRemover.getId());\n"
                    + "        FacesUtil.addMessageInfo(\"Informação\", \"O objeto foi excluído.\");\n"
                    + "        lista = null;\n"
                    + "    }\n"
                    + "\n"
                    + "    public void salvar() {\n"
                    + "        service.salvar(entidade);\n"
                    + "        if (novo) {\n"
                    + "            FacesUtil.addMessageInfo(\"Informação\", \"O registro foi inserido.\");\n"
                    + "        } else {\n"
                    + "            FacesUtil.addMessageInfo(\"Informação\", \"O registro foi alterado.\");\n"
                    + "        }\n"
                    + "        lista = null;\n"
                    + "    }\n"
                    + "\n"
                    + "    public void cancelar() {\n"
                    + "        entidade = null;\n"
                    + "        FacesUtil.addMessageWarn(\"Aviso\", \"A operação foi cancelada\");\n"
                    + "    }\n"
                    + "\n"
                    + "    public void filtrar() {\n"
                    + "        if (filtro != null) {\n"
                    + "            lista = service.lista(); //TODO Filtrar a \n"
                    + "        }\n"
                    + "    }\n"
                    + "\n"
                    + "    public Converter getConverter() {\n"
                    + "        return new ConverterGenerico(service);\n"
                    + "    }\n"
                    + "\n"
                    + "}\n"
                    + ""
                    + ""
                    + "");
            fw.close();
        } catch (Exception ex) {

        }

    }

}
