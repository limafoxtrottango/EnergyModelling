package edu.bitsgoa.startup;
/**
 * This class first checks for the presence of nvcc in user's computer. If not, then it freezes the plug-in. Otherwise, it extracts
 * the contents of the "Executables" folder into a temporary location, and changes the permission for each of them using the 
 * chmod u+x ... command.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IStartup;
import org.osgi.framework.Bundle;

import edu.bitsgoa.driver.EnergyEstimator;
import edu.bitsgoa.driver.Preparation;
import edu.bitsgoa.utilities.BetterRunProcess;
import edu.bitsgoa.views.DisplayCustomConsole;

public class StartCheck  implements IStartup {

	public static String path_bandwidthExec;
	public static String path_devicequeryExec;
	public static String path_kernellaunchoverheadExec;
	public static String path_memorylatencyExec;
	public static URL 	url_bandwidth,
						url_deviceQuery,
						url_kernelLaunchOverhead,
						url_memoryLatency,
						url_addf32,
						url_andb32,
						url_divf32,
						url_divs32,
						url_mad,
						url_mulf32,
						url_sqrtcvt,
						url_subs32,
						url_throughput_csv;

	/**
	 * Called when Eclipse starts-up. First, it checks for the presence of the nvcc. If absent, then the plug-in is frozen. Otherwise, the 
	 * Executables are extracted at a temporary location and their associated permissions to run are changed using changePermissions()
	 *@param
	 *@return
	 */
	public void earlyStartup() {
		//Check if nvcc is present. 
		String command="nvcc --version";
		Runtime run = Runtime.getRuntime();
		Process pr;

		try {
			//If nvcc is absent, then throw an error and go to the catch block. Otherwise, continue
			pr = run.exec(command);
			pr.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			//Print-out the nvcc version
			String line=buf.readLine();
			DisplayCustomConsole.display(buf.readLine(),true);
			Preparation.return_val=true;
			//Extract the Executables
			Bundle bundle = Platform.getBundle("edu.bitsgoa.EnergyModelling");
			url_bandwidth = FileLocator.find(bundle, new Path("/Executables/bandWidth.out"), null);
			url_deviceQuery = FileLocator.find(bundle, new Path("/Executables/deviceQuery.out"), null);
			url_kernelLaunchOverhead = FileLocator.find(bundle, new Path("/Executables/empty"), null);
			url_memoryLatency = FileLocator.find(bundle, new Path("/Executables/memLatency.out"), null);
			url_addf32 = FileLocator.find(bundle, new Path("/Executables/instLatency_addf32"), null);
			url_andb32 = FileLocator.find(bundle, new Path("/Executables/instLatency_andb32"), null);
			url_divf32 = FileLocator.find(bundle, new Path("/Executables/instLatency_divf32"), null);
			url_divs32 = FileLocator.find(bundle, new Path("/Executables/instLatency_divs32"), null);
			url_mad = FileLocator.find(bundle, new Path("/Executables/instLatency_mad"), null);
			url_mulf32 = FileLocator.find(bundle, new Path("/Executables/instLatency_mulf32"), null);
			url_sqrtcvt = FileLocator.find(bundle, new Path("/Executables/instLatency_sqrtcvt"), null);
			url_subs32 = FileLocator.find(bundle, new Path("/Executables/instLatency_subs32"), null);
			url_throughput_csv=FileLocator.find(bundle,new Path("/Executables/throughput.csv"), null);

			try {
				url_bandwidth = FileLocator.toFileURL(url_bandwidth);
				url_deviceQuery = FileLocator.toFileURL(url_deviceQuery);
				url_kernelLaunchOverhead = FileLocator.toFileURL(url_kernelLaunchOverhead);
				url_memoryLatency = FileLocator.toFileURL(url_memoryLatency);
				url_addf32 = FileLocator.toFileURL(url_addf32);
				url_andb32= FileLocator.toFileURL(url_andb32);
				url_divf32= FileLocator.toFileURL(url_divf32);
				url_divs32= FileLocator.toFileURL(url_divs32);
				url_mad= FileLocator.toFileURL(url_mad);
				url_mulf32= FileLocator.toFileURL(url_mulf32);
				url_sqrtcvt= FileLocator.toFileURL(url_sqrtcvt);
				url_subs32= FileLocator.toFileURL(url_subs32);
				url_throughput_csv=FileLocator.toFileURL(url_throughput_csv);

				//The following four variables now contain the temporary path of extraction. We do not need to store paths of any other executable
				path_bandwidthExec=url_bandwidth.toString();
				path_devicequeryExec=url_deviceQuery.toString();
				path_kernellaunchoverheadExec=url_kernelLaunchOverhead.toString();
				path_memorylatencyExec=url_memoryLatency.toString();

				//Change permissions for all files
				changePermissions("bandWidth.out",path_bandwidthExec.substring(path_bandwidthExec.indexOf('/'),path_bandwidthExec.lastIndexOf('/')+1));
				changePermissions("deviceQuery.out",path_bandwidthExec.substring(path_bandwidthExec.indexOf('/'),path_bandwidthExec.lastIndexOf('/')+1));
				changePermissions("empty",path_bandwidthExec.substring(path_bandwidthExec.indexOf('/'),path_bandwidthExec.lastIndexOf('/')+1));
				changePermissions("memLatency.out",path_bandwidthExec.substring(path_bandwidthExec.indexOf('/'),path_bandwidthExec.lastIndexOf('/')+1));
				changePermissions("instLatency_addf32",path_bandwidthExec.substring(path_bandwidthExec.indexOf('/'),path_bandwidthExec.lastIndexOf('/')+1));
				changePermissions("instLatency_andb32",path_bandwidthExec.substring(path_bandwidthExec.indexOf('/'),path_bandwidthExec.lastIndexOf('/')+1));
				changePermissions("instLatency_divf32",path_bandwidthExec.substring(path_bandwidthExec.indexOf('/'),path_bandwidthExec.lastIndexOf('/')+1));
				changePermissions("instLatency_divs32",path_bandwidthExec.substring(path_bandwidthExec.indexOf('/'),path_bandwidthExec.lastIndexOf('/')+1));
				changePermissions("instLatency_mad",path_bandwidthExec.substring(path_bandwidthExec.indexOf('/'),path_bandwidthExec.lastIndexOf('/')+1));
				changePermissions("instLatency_mulf32",path_bandwidthExec.substring(path_bandwidthExec.indexOf('/'),path_bandwidthExec.lastIndexOf('/')+1));
				changePermissions("instLatency_sqrtcvt",path_bandwidthExec.substring(path_bandwidthExec.indexOf('/'),path_bandwidthExec.lastIndexOf('/')+1));
				changePermissions("instLatency_subs32",path_bandwidthExec.substring(path_bandwidthExec.indexOf('/'),path_bandwidthExec.lastIndexOf('/')+1));
				changePermissions("throughput.csv",path_bandwidthExec.substring(path_bandwidthExec.indexOf('/'),path_bandwidthExec.lastIndexOf('/')+1));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				DisplayCustomConsole.display(e.toString(),true);
			}

		}catch (IOException e) {
			//disable all commands since no further task can be done, prompt user to install nvcc.
			DisplayCustomConsole.display("NVCC not found. Download from: https://developer.nvidia.com/cuda-downloads",true);
			EnergyEstimator.return_val=false;
			Preparation.return_val=false;
			
		} catch (InterruptedException e) {
			DisplayCustomConsole.display(e.toString(),true);
		}
	}
	/**
	 * Changes permission associated with a particular executable so that it can be run from within the program
	 * @param filename	executable for which permission has to be changed
	 * @param path	
	 * @return 
	 */
	public static void changePermissions(String filename,String path){
		String[] cmd=new String[3];
		cmd[0]="chmod";
		cmd[1]="+x";
		cmd[2]=filename;
		BetterRunProcess process=new BetterRunProcess(cmd,path,1,0,0,"");
		process.run();
	}
}


