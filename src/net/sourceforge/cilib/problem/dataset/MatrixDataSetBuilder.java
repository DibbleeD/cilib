/*
 * MatrixDataSetBuilder.java
 * 
 * Created on Feb 28, 2006
 *
 * Copyright (C) 2003 - 2006 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.sourceforge.cilib.problem.dataset;

import java.io.EOFException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

import net.sourceforge.cilib.container.Matrix;

/**
 * TODO: This needs to implement the reading of a matrix as needed by MappingProblem
 * @author Gary Pampara
 *
 */
public class MatrixDataSetBuilder extends DataSetBuilder {
	
	private Matrix<Double> matrix;
	private int numvectors = 0;
	private int M = -1;
	private int D = -1;
	
	/**
	 * 
	 *
	 */
	public MatrixDataSetBuilder() {
	}

	
	/**
	 * 
	 */
	@Override
	public void initialise() {
		
		try {
			InputStream is = this.getDataSet(0).getInputStream();
			
			StreamTokenizer tok = new StreamTokenizer(new InputStreamReader(is));

			if(tok.nextToken() != StreamTokenizer.TT_NUMBER)
				throw new IllegalStateException("Expected an integer number as the first token in the dataset");

			numvectors = (int) tok.nval;

			if(numvectors <= 0)
				throw new IllegalStateException("Must have a positive number of vectors in input file");

			if(tok.nextToken() != StreamTokenizer.TT_NUMBER)
				throw new IllegalStateException("Expected an integer number as the second token in the dataset");

			M = (int) tok.nval;

			if(M <= 0)
				throw new IllegalStateException("Need to have a positive number as the input dimensions");

			matrix = new Matrix<Double>(numvectors, M);
			
			if(tok.nextToken() != StreamTokenizer.TT_NUMBER)
				throw new IllegalStateException("Expected an integer number as the third token in the dataset");

			D = (int) tok.nval;

			if(D <= 0)
				throw new IllegalStateException("Need to have a positive number as the input dimensions");
				
			if(!(D <= M))
				throw new IllegalStateException("Output dimension must be less than input dimension");

			for(int i = 0; i < numvectors; i++) {
				for(int m = 0; m < M; m++) {
					int tok_ret = tok.nextToken();
					while(tok_ret != StreamTokenizer.TT_NUMBER)	{
						switch(tok_ret) {
						case StreamTokenizer.TT_EOF:
							throw new EOFException();
						case StreamTokenizer.TT_WORD:
							throw new IllegalStateException("Only numerical input expected (line " + tok.lineno() + ")");
						}
					}

					matrix.set(i, m, tok.nval);
				}
			}
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}		
	}
	
	
	/**
	 * Get the constructed {@see net.sourceforge.cilib.container.Matrix Matrix}
	 * 
	 * @return
	 */
	public Matrix<Double> getMatrix() {
		return this.matrix;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public int getD() {
		return D;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public int getM() {
		return M;
	}
	
}