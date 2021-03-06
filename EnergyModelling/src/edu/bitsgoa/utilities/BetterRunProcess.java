package edu.bitsgoa.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.bitsgoa.views.DisplayCustomConsole;

public class BetterRunProcess implements Runnable {
	private String[] cmd;
	private String path;
	private int printToConsole;
	private int printToExternalFile;
	private int append;
	private String filename;
	
	public String[] getCmd() {
		return cmd;
	}

	public void setCmd(String[] cmd) {
		this.cmd = cmd;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getPrintToConsole() {
		return printToConsole;
	}

	public void setPrintToConsole(int printToConsole) {
		this.printToConsole = printToConsole;
	}

	public int getPrintToExternalFile() {
		return printToExternalFile;
	}

	public void setPrintToExternalFile(int printToExternalFile) {
		this.printToExternalFile = printToExternalFile;
	}

	public int getAppend() {
		return append;
	}

	public void setAppend(int append) {
		this.append = append;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public BetterRunProcess(){
		
	}
	
	public BetterRunProcess(String[] cmd,String path,int printToConsole,int printToExternalFile,int append,String filename){
		this.cmd=cmd;
		this.path=path;
		this.printToConsole=printToConsole;
		this.printToExternalFile=printToExternalFile;
		this.append=append;
		this.filename=filename;
	}
	@Override
	public void run() {
		ProcessBuilder builder;
		if(cmd.length==1)	builder=new ProcessBuilder(cmd[0]);
		else if(cmd.length==2)	builder=new ProcessBuilder(cmd[0],cmd[1]);
		else if(cmd.length==3)	builder=new ProcessBuilder(cmd[0],cmd[1],cmd[2]);
		else if(cmd.length==4)	builder=new ProcessBuilder(cmd[0],cmd[1],cmd[2],cmd[3]);
		else	builder=new ProcessBuilder(cmd[0],cmd[1],cmd[2],cmd[3],cmd[4]);
		builder.directory(new File(path));
		try {
			Process pr=builder.start();
			if(printToConsole==1) printToConsole(pr);
			if(printToExternalFile==1) printToExternalFile(pr,filename,append);
		} catch (IOException e) {
			DisplayCustomConsole.display(e.toString(),true);
		}
	}
	/**
	 * Writes the result of running a process to the custom console using DisplayCustomConsole.display()
	 * @param pr a process object as Process proc=processBuilderObject.start();
	 * @return
	 */
	public void printToConsole(Process pr){
		BufferedReader br_output=new BufferedReader(new InputStreamReader(pr.getInputStream()));
		BufferedReader br_err=new BufferedReader(new InputStreamReader(pr.getErrorStream()));
		String line;
		try {
			while((line=br_output.readLine())!=null){
				DisplayCustomConsole.display(line+"\n",true);
			}
			br_output.close();
			while((line=br_err.readLine())!=null){
				DisplayCustomConsole.display(line+"\n",true);
			}
			br_err.close();
			br_err.close();
		} catch (IOException e) {
			DisplayCustomConsole.display(e.toString(),true);
		}
	}
	/**
	 * Writes the results of running a process to external text file 
	 * @param proc	a process object as Process proc=processBuilderObject.start();
	 * @param filename	name of the file in which to write data
	 * @param append	1 if the file already exists, then just append data to it. If 0, then create a new text file and write to it
	 * @return
	 */
	public void printToExternalFile(Process proc,String filename,int append){
		String path="/home/"+System.getProperty("user.name")+"/.eclipse"+"/models/"+filename;
		BufferedReader br_output = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		BufferedReader br_err=new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		String line;
		try {
			if(append==0){	//implies that a new file is to be created and written into
				FileWriter fw = new FileWriter(path,false);
				while ( (line = br_output.readLine()) != null){
					fw.write(line+"\n");
				}
				while((line=br_err.readLine())!=null){
					fw.write(line);
				}
				fw.close();
			}
			else{	//implies that data has to be written into an existing file
				FileWriter fw = new FileWriter(path,true);
				while ( (line = br_output.readLine()) != null){
					fw.write(line+"\n");
				}
				while((line=br_err.readLine())!=null){
					fw.write(line);
				}
				fw.close();
			}
		} catch (IOException e) {
			DisplayCustomConsole.display(e.toString(),true);
		}
	}

}
