/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2016, 2019 by the contributors of the JetUML project.
 *
 * See: https://github.com/prmr/JetUML
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package ca.mcgill.cs.jetuml.diagram;

import static ca.mcgill.cs.jetuml.application.ApplicationResources.RESOURCES;

import ca.mcgill.cs.jetuml.diagram.builder.ClassDiagramBuilder;
import ca.mcgill.cs.jetuml.diagram.builder.DiagramBuilder;
import ca.mcgill.cs.jetuml.diagram.builder.ObjectDiagramBuilder;
import ca.mcgill.cs.jetuml.diagram.builder.SequenceDiagramBuilder;
import ca.mcgill.cs.jetuml.diagram.builder.StateDiagramBuilder;
import ca.mcgill.cs.jetuml.diagram.builder.UseCaseDiagramBuilder;
import ca.mcgill.cs.jetuml.views.DiagramViewer;
import ca.mcgill.cs.jetuml.views.SequenceDiagramViewer;

/**
 * The different types of UML diagrams supported by 
 * this application.
 */
public enum DiagramType
{
	CLASS(
			ClassDiagram.class, 
			ClassDiagramBuilder.class, 
			new DiagramViewer(), 
			RESOURCES.getString("classdiagram.file.extension")), 
	
	SEQUENCE(
			SequenceDiagram.class, 
			SequenceDiagramBuilder.class, 
			new SequenceDiagramViewer(),
			RESOURCES.getString("sequencediagram.file.extension")), 
	
	STATE(
			StateDiagram.class, 
			StateDiagramBuilder.class, 
			new DiagramViewer(),
			RESOURCES.getString("statediagram.file.extension")), 
	
	OBJECT(
			ObjectDiagram.class, 
			ObjectDiagramBuilder.class, 
			new DiagramViewer(),
			RESOURCES.getString("objectdiagram.file.extension")), 
	
	USECASE(
			UseCaseDiagram.class, 
			UseCaseDiagramBuilder.class, 
			new DiagramViewer(),
			RESOURCES.getString("usecasediagram.file.extension"));
	
	private final Class<?> aClass;
	private final Class<?> aBuilderClass;
	private final DiagramViewer aViewer;
	private final String aFileExtension;
	
	DiagramType(Class<?> pClass, Class<?> pBuilderClass, DiagramViewer pViewer, String pFileExtension)
	{
		aClass = pClass;
		aBuilderClass = pBuilderClass;
		aViewer = pViewer;
		aFileExtension = pFileExtension;
	}
	
	/**
	 * @return The file extension for this type of diagram.
	 */
	public String getFileExtension()
	{
		return aFileExtension;
	}
	
	/**
	 * @param pDiagram The diagram whose type we want to check.
	 * @return The type of pDiagram.
	 * @pre pDiagram != null
	 */
	public static DiagramType typeOf(Diagram pDiagram)
	{
		assert pDiagram != null;
		for( DiagramType type : values())
		{
			if( pDiagram.getClass() == type.aClass )
			{
				return type;
			}
		}
		assert false;
		return null;
	}
	
	/**
	 * @return A new instance of the diagram type that corresponds to this value.
	 */
	public Diagram newInstance()
	{
		try
		{
			return (Diagram) aClass.getDeclaredConstructor().newInstance();
		}
		catch(ReflectiveOperationException exception)
		{
			assert false;
			return null;
		}
	}
	
	/**
	 * @param pDiagram The diagram for which we want to build a builder.
	 * @return A new instance of a builder for this diagram type.
	 * @pre pDiagram != null
	 */
	public static DiagramBuilder newBuilderInstanceFor(Diagram pDiagram)
	{
		assert pDiagram != null;
		try
		{
			return (DiagramBuilder) typeOf(pDiagram).aBuilderClass.getDeclaredConstructor(Diagram.class).newInstance(pDiagram);
		}
		catch(ReflectiveOperationException exception)
		{
			assert false;
			return null;
		}
	}
	
	/**
	 * @return The name of the handler, which is the simple name of the corresponding
	 * class in all lower case.
	 */
	public String getName()
	{
		return aClass.getSimpleName().toLowerCase();
	}
	
	/**
	 * @param pDiagram The diagram for which we want a viewer.
	 * @return The DiagramViewer instance registered for this type of diagram.
	 * @pre pDiagram != null;
	 */
	public static DiagramViewer viewerFor(Diagram pDiagram) 
	{
		assert pDiagram != null;
		return typeOf(pDiagram).aViewer;
	}
}
