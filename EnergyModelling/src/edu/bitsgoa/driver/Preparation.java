/**
 * This class checks for the existence of certain files, and if absent, generates them by running executables using the BetterRunProcess class.
 *Micro-benchmarking of user's hardware is also done in this class. This class is a handler class for the menu command "Prepare". That is, when
 *the user click on "Prepare" while using the plug-in, this class is called.
 */
package edu.bitsgoa.driver;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import edu.bitsgoa.properties.Parameters;
import edu.bitsgoa.regression.KernelLaunchOverhead;
import edu.bitsgoa.regression.MemoryLatency;
import edu.bitsgoa.regression.Microbenchmark;
import edu.bitsgoa.startup.StartCheck;
import edu.bitsgoa.views.DisplayCustomConsole;

public class Preparation extends AbstractHandler implements IHandler {

	public static boolean return_val;
	public static String path_home;
	public static boolean used;	

	private static boolean[] existenceArray;	
	private static int bvar1=0;	
	private static int bvar2=0;	
	private static String path_executables;
	
	public void initialize(){
		path_executables=StartCheck.path_devicequeryExec.substring(StartCheck.path_devicequeryExec.indexOf('/'),StartCheck.path_devicequeryExec.lastIndexOf('/'))+"/";
		used=true;
		return_val=false;
		path_home="/home/"+System.getProperty("user.name")+"/.eclipse"+"/models/";
		existenceArray=new boolean[19];
		for(int i=0;i<19;i++)	existenceArray[i]=false;
		mkdir();
		filesExist();
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		initialize();
		Executor executor=Executors.newSingleThreadExecutor();
		try{
			if(!existenceArray[1]){	
				String[] cmd=new String[1];
				cmd[0]="./deviceQuery.out";
				DisplayCustomConsole.display("Gathering GPU Configuration Information...",false);
				edu.bitsgoa.utilities.BetterRunProcess exp=new edu.bitsgoa.utilities.BetterRunProcess(cmd,path_executables,0,1,0,"deviceQuery.txt");
				DisplayCustomConsole.display("Done",true);
				executor.execute(exp);
			}
			if(!existenceArray[0]){	
				String[] cmd=new String[1];
				cmd[0]="./bandWidth.out";
				DisplayCustomConsole.display("Gathering GPU Memory Bandwidth Information...", false);
				edu.bitsgoa.utilities.BetterRunProcess exp=new edu.bitsgoa.utilities.BetterRunProcess(cmd,path_executables,0,1,0,"bandWidth.txt");
				DisplayCustomConsole.display("Done",true);
				executor.execute(exp);
			}
			if(!existenceArray[2]){
				KernelLaunchOverhead klo=new KernelLaunchOverhead(bvar1, bvar2);
				executor.execute(klo);
			}
			MemoryLatency ml=new MemoryLatency();
			executor.execute(ml);
			
			Microbenchmark mb=new Microbenchmark(existenceArray);
			executor.execute(mb);

		}catch(Exception e){
			DisplayCustomConsole.display(e.toString(),true);
		}
		
		if(Parameters.timesused==0){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					DisplayCustomConsole.display("Fill-in the Input Parameters: Project->Properties->Energy Estimation",true);
					}
			});
		}
		else{
			executor.execute(new Runnable() {
				@Override
				public void run() {
					DisplayCustomConsole.display("Energy Estimation Can Now Be Performed",true);				
				}
			});
		}
		return null;
	}
	/**
	 * Check if certain files exist at path_home. If yes, then make the corresponding entry in existenceArray as true.
	 * @param null
	 * @return null
	 */
	public static void filesExist(){
		boolean kernelLaunchOverhead,kernelLaunchOverheadModel;
		
		kernelLaunchOverhead = new File(path_home,"kernelLaunchOverhead.txt").exists();
		kernelLaunchOverheadModel=new File(path_home,"KernelLaunchOverheadModel.txt").exists();	
		
		existenceArray[0] = new File(path_home,"bandWidth.txt").exists();
		existenceArray[1] = new File(path_home,"deviceQuery.txt").exists();	
		existenceArray[3]=new File(path_home,"times_addf32.txt").exists();
		existenceArray[4]=new File(path_home,"times_andb32.txt").exists();
		existenceArray[5]=new File(path_home,"times_divf32.txt").exists();
		existenceArray[6]=new File(path_home,"times_divs32.txt").exists();
		existenceArray[7]=new File(path_home,"times_mad.txt").exists();
		existenceArray[8]=new File(path_home,"times_mulf32.txt").exists();
		existenceArray[9]=new File(path_home,"times_sqrtcvt.txt").exists();
		existenceArray[10]=new File(path_home,"times_subs32.txt").exists();
		existenceArray[11]=new File(path_home,"av_times_addf32.txt").exists();
		existenceArray[12]=new File(path_home,"av_times_andb32.txt").exists();
		existenceArray[13]=new File(path_home,"av_times_divf32.txt").exists();
		existenceArray[14]=new File(path_home,"av_times_divs32.txt").exists();
		existenceArray[15]=new File(path_home,"av_times_mad.txt").exists();
		existenceArray[16]=new File(path_home,"av_times_mulf32.txt").exists();
		existenceArray[17]=new File(path_home,"av_times_sqrtcvt.txt").exists();
		existenceArray[18]=new File(path_home,"av_times_subs32.txt").exists();

		if(kernelLaunchOverhead && !kernelLaunchOverheadModel){
			existenceArray[2]=false;
			bvar1=1;
			bvar2=0;
		}
		else if(!kernelLaunchOverhead && kernelLaunchOverheadModel){
			existenceArray[2]=false;
			bvar1=0;
			bvar2=0;
		}
		else if(!kernelLaunchOverhead && !kernelLaunchOverheadModel){
			existenceArray[2]=false;
			bvar1=0;
			bvar2=0;
		}
		else existenceArray[2]=true;
		
	}
	
		/** Make a directory at path_home
	 * @param null
	 * @return null
	 */
	public static void mkdir(){
		new File(path_home).mkdir();
	}
	
	public boolean isEnabled(){
		return return_val;
	}
}