package com.example.idcard_demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wintone.idcard.android.AuthService;
import wintone.idcard.android.IDCardAPI;
import wintone.idcard.android.RecogParameterMessage;
import wintone.idcard.android.ResultMessage;
import wintone.idcard.android.TimeService;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.wintone.lisence.CDKey;
import com.wintone.lisence.Common;
import com.wintone.lisence.DateAuthFileOperate;
import com.wintone.lisence.DeviceFP;
import com.wintone.lisence.MachineCode;
import com.wintone.lisence.ModeAuthFileOperate;
import com.wintone.lisence.ModeAuthFileResult;
import com.wintone.lisence.ProcedureAuthOperate;
import com.wintone.lisence.SqliteHelperUtils;
import com.wintone.lisence.VersionAuthFileOperate;
import com.wintone.lisence.WintoneLSCOperateTools;
import com.wintone.lisence.WintoneLSCXMLInformation;

public class RecogService extends Service {
	private recogBinder rgBinder;
	private int ReturnInitIDCard = -1;
	public static int nTypeInitIDCard = 0;
	private int ReturnAuthority = -10015;
	private String mcode;
	private Common common = new Common();
	private String idcardpath = this.common.getSDPath() + "/AndroidWT/IDCard/";
	private String rootpath = this.common.getSDPath() + "/AndroidWT";
	private ResultMessage resultMessage;
	private Boolean isTF = Boolean.valueOf(false);
	private static Intent service;
	private ModeAuthFileResult mafr = new ModeAuthFileResult();
	private ModeAuthFileResult mafr1 = new ModeAuthFileResult();
	public static boolean isRecogByPath = true;
	public static boolean isUpdateLSC = true;
	private static String query_old_lsc = "select * from old_lsc where _id=1";
	private String miwenxml = null;
	private ModeAuthFileOperate mafo = new ModeAuthFileOperate();

	private int String2Int(String stri) {
		int nRet = 0;
		if ((stri != null) && (!(stri.equals("")))) {
			try {
				nRet = Integer.parseInt(stri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return nRet;
	}

	public String readtxt() throws IOException {
		Common common = new Common();
		String paths = common.getSDPath();
		if ((paths == null) || (paths.equals(""))) {
			return "";
		}
		String fullpath = paths + "/AndroidWT/idcard.cfg";
		File file = new File(fullpath);
		if (!(file.exists())) {
			return "";
		}
		FileReader fileReader = new FileReader(fullpath);
		BufferedReader br = new BufferedReader(fileReader);
		String str = "";
		String r = br.readLine();
		while (r != null) {
			str = str + r;
			r = br.readLine();
		}
		br.close();
		fileReader.close();
		return str;
	}

	public void onCreate() {
		super.onCreate();
		boolean isSIM = false;
		this.ReturnInitIDCard = -10003;
		this.rgBinder = new recogBinder();

		if (service == null) {
			service = new Intent(this, TimeService.class);
			startService(service);
		}

		this.miwenxml = readSDCardFile();

		if ((this.miwenxml != null) && (isUpdateLSC)) {
			this.mafr = this.mafo.ReadAuthFile(this.miwenxml);
			this.mafr1 = this.mafo.ReadAuthFile(readAssetFile(getAssets(), "authmode.lsc"));

			if (this.mafr1.isCheckPRJMode("11")) {
				if ((!(this.mafr.prjmode_version[0].equals(this.mafr1.prjmode_version[0])))
						|| (!(this.mafr.prjmode_closingdate[0]
								.equals(this.mafr1.prjmode_closingdate[0])))
						|| (!(this.mafr.prjmode_app_name[0].equals(this.mafr1.prjmode_app_name[0])))
						|| (!(this.mafr.prjmode_company_name[0]
								.equals(this.mafr1.prjmode_company_name[0])))
						|| (!(this.mafr.prjmode_packagename[0]
								.equals(this.mafr1.prjmode_packagename[0])))
						|| (!(this.mafr.devcode.equals(this.mafr1.devcode)))) {
					File file = new File(this.idcardpath + "authmode.lsc");

					if (file.exists()) {
						file.delete();
						this.miwenxml = null;
					}
				}

			} else {
				this.miwenxml = readAssetFile(getAssets(), "authmode.lsc");
			}
		}
		if (this.miwenxml == null) {
			this.miwenxml = readAssetFile(getAssets(), "authmode.lsc");
		}
		this.mafr = this.mafo.ReadAuthFile(this.miwenxml);

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
		StringBuilder sb = new StringBuilder();
		sb.append(telephonyManager.getDeviceId());
		String deviceId = sb.toString();
		StringBuilder sb1 = new StringBuilder();
		sb1.append(Settings.Secure.getString(getContentResolver(), "android_id"));
		String androId = sb1.toString();
		StringBuilder sb2 = new StringBuilder();
		sb2.append(telephonyManager.getSimSerialNumber());
		String simId = sb2.toString();
		try {
			String versionName = null;
			InputStream iStream = getAssets().open("version.txt");
			int size_is = iStream.available();
			byte[] byte_new = new byte[size_is];
			iStream.read(byte_new);
			iStream.close();
			versionName = new String(byte_new);

			String versiontxt = "";
			String paths = this.common.getSDPath();
			if ((paths != null) && (!(paths.equals("")))) {
				String versionpath = this.idcardpath + "version.txt";
				File versionfile = new File(versionpath);
				if (versionfile.exists()) {
					FileReader fileReader = new FileReader(versionpath);
					BufferedReader br = new BufferedReader(fileReader);
					String r = br.readLine();
					while (r != null) {
						versiontxt = versiontxt + r;
						r = br.readLine();
					}
					br.close();
					fileReader.close();
				}
				if (!(versionName.equals(versiontxt))) {
					File dir = new File(this.rootpath);
					if (!(dir.exists())) {
						dir.mkdirs();
					}
					File dir1 = new File(this.idcardpath);
					if (!(dir1.exists())) {
						dir1.mkdirs();
					}
					if (AuthService.isHolder) {
						copyHolderBigFile();
					} else {
						copyBigFile();
					}
					copyDataBase();
					System.out.println("Copy assets files. versionName:" + versionName
							+ " versiontxt:" + versiontxt);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ((this.miwenxml != null) && (this.mafr.isTF("11"))) {
			this.isTF = Boolean.valueOf(true);
			IDCardAPI idCard = new IDCardAPI();
			DeviceFP deviceFP = new DeviceFP();
			this.ReturnInitIDCard = idCard.InitIDCardTF("1002", nTypeInitIDCard, this.idcardpath,
					deviceFP);

		} else {
			Common common = new Common();
			String[] str = { "", "", "", "", "", "", "", "", "", "", "", "" };
			DeviceFP deviceFP = new DeviceFP();
			Boolean bool = Boolean.valueOf(false);
			telephonyManager = (TelephonyManager) getSystemService("phone");
			try {
				if ((this.miwenxml != null) && (this.mafr.isSIM("11"))) {
					if ((simId != null) && (!(simId.equals(""))) && (!(simId.equals("null")))) {
						deviceId = simId;
						isSIM = true;
					} else {
						this.ReturnInitIDCard = -10501;
						return;
					}
				}
				MachineCode machineCode = new MachineCode();
				this.mcode = machineCode.MachineNO("1.0", deviceId, androId, simId);

				if ((this.miwenxml != null) && (this.mafr.isCheckPRJMode("11"))) {
					bool = Boolean.valueOf(true);
					deviceFP.deviceid = "DeviceIdIsNull";
					this.ReturnAuthority = 0;
				}

				String versionInitFilePatnString = Environment.getExternalStorageDirectory()
						+ "/AndroidWT/wtversioninit.lsc";
				File versionInitFile = new File(versionInitFilePatnString);
				if (versionInitFile.exists()) {
					if (telephonyManager.getDeviceId() == null) {
						bool = Boolean.valueOf(true);
						this.ReturnAuthority = 0;
					}

					if (telephonyManager.getDeviceId().equals(
							readInitFileString(versionInitFilePatnString))) {
						deviceFP.deviceid = readInitFileString(versionInitFilePatnString);
						bool = Boolean.valueOf(true);
						this.ReturnAuthority = 0;
					}

				}

				String oldDateInitFilePath = Environment.getExternalStorageDirectory()
						+ "/wintone/idcarddateinit.lsc";
				String newDateInitFilePath = Environment.getExternalStorageDirectory()
						+ "/AndroidWT/wtdateinit.lsc";
				File oldDateInitFile = new File(oldDateInitFilePath);
				File newDateInitFile = new File(newDateInitFilePath);
				if ((newDateInitFile.exists()) || (oldDateInitFile.exists())) {
					if (telephonyManager.getDeviceId() == null) {
						bool = Boolean.valueOf(true);
						this.ReturnAuthority = 0;
					}
					if (oldDateInitFile.exists()) {
						if (telephonyManager.getDeviceId().equals(
								readInitFileString(oldDateInitFilePath))) {
							deviceFP.deviceid = readInitFileString(oldDateInitFilePath);
							bool = Boolean.valueOf(true);
							this.ReturnAuthority = 0;
						}

					}

					if (telephonyManager.getDeviceId().equals(
							readInitFileString(newDateInitFilePath))) {
						deviceFP.deviceid = readInitFileString(newDateInitFilePath);
						bool = Boolean.valueOf(true);
						this.ReturnAuthority = 0;
					}

				}

				ProcedureAuthOperate pao = new ProcedureAuthOperate(this);
				String path = pao.getOriginalAuthFilePathByProjectType("11");
				String wintoneLSCFilePathString = common.getSDPath() + "/AndroidWT/wt.lsc";
				File oldLscFile = new File(path);
				File wintoneLSCFile = new File(wintoneLSCFilePathString);
				CDKey cdKey = new CDKey();
				if ((oldLscFile.exists()) || (wintoneLSCFile.exists())
						|| (!(wintoneLSCFile.exists()))) {
					if (oldLscFile.exists()) {
						str = pao.readOriginalAuthFileContent(path);
						bool = Boolean.valueOf(cdKey.checkjhm(str[2], this.mcode, str[1]));
						if ((deviceFP.deviceid == null) || (deviceFP.deviceid.equals(" "))
								|| (deviceFP.deviceid.equals("null"))) {
							if (deviceId.equals(str[3]))
								deviceFP.deviceid = str[3];
							else {
								bool = Boolean.valueOf(false);
							}
						}

						if ((!(bool.booleanValue())) && (str.length >= 8)) {
							if ((str[8] != null) && (str[7] != null))
								bool = Boolean.valueOf(cdKey.checkjhm(str[8], this.mcode, str[7]));
							if ((deviceFP.deviceid == null) || (deviceFP.deviceid.equals(" "))
									|| (deviceFP.deviceid.equals("null"))) {
								if (deviceId.equals(str[9]))
									deviceFP.deviceid = str[9];
								else {
									bool = Boolean.valueOf(false);
								}
							}

						}

					} else {
						SqliteHelperUtils sqliteHelperUtils = new SqliteHelperUtils(
								getApplicationContext(), "wt.db", 2);
						String oldwtlsc = sqliteHelperUtils.queryData(query_old_lsc, null);
						if ((oldwtlsc != null) && (!(oldwtlsc.equals("")))) {
							try {
								common = new Common();
								String SysCertVersion = "wtversion5_5";
								String aesjie = common.getSrcPassword(oldwtlsc, SysCertVersion);
								String[] result = aesjie.split(",");
								bool = Boolean.valueOf(cdKey.checkjhm(result[2], this.mcode,
										result[1]));
								if ((deviceFP.deviceid == null) || (deviceFP.deviceid.equals(" "))
										|| (deviceFP.deviceid.equals("null"))) {
									if (deviceId.equals(str[3]))
										deviceFP.deviceid = str[3];
									else {
										bool = Boolean.valueOf(false);
									}
								}

								if ((!(bool.booleanValue())) && (str.length >= 8)) {
									if ((str[8] != null) && (str[7] != null))
										bool = Boolean.valueOf(cdKey.checkjhm(str[8], this.mcode,
												str[7]));
									if ((deviceFP.deviceid == null)
											|| (deviceFP.deviceid.equals(" "))
											|| (deviceFP.deviceid.equals("null"))) {
										if (deviceId.equals(str[9]))
											deviceFP.deviceid = str[9];
										else {
											bool = Boolean.valueOf(false);
										}
									}

								}

								File originalAuthRootFile = new File(Environment
										.getExternalStorageDirectory().toString() + "/wintone/");
								if (!(originalAuthRootFile.exists()))
									originalAuthRootFile.mkdirs();
								FileOutputStream outStream = new FileOutputStream(path);
								outStream.write(oldwtlsc.getBytes());
								outStream.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							WintoneLSCXMLInformation wlxi = null;

							TelephonyManager telephonyManager2 = (TelephonyManager) getSystemService("phone");

							if ((telephonyManager2.getDeviceId() != null)
									&& (!(telephonyManager2.getDeviceId().equals("")))) {
								wlxi = WintoneLSCOperateTools.ReadAuthFile(telephonyManager2
										.getDeviceId());
							} else {
								wlxi = WintoneLSCOperateTools.ReadAuthFile(Settings.Secure
										.getString(getContentResolver(), "android_id"));
							}

							if (wlxi != null) {
								bool = Boolean.valueOf(cdKey.checkjhm(wlxi.anoString, this.mcode,
										wlxi.snoString));
								if ((deviceFP.deviceid == null) || (deviceFP.deviceid.equals(" "))
										|| (deviceFP.deviceid.equals("null"))) {
									deviceFP.deviceid = wlxi.deviceIdString;
								}
							}
						}
					}
				}

			} catch (Exception e) {
				bool = Boolean.valueOf(false);
			}

			try {
				if (bool.booleanValue()) {
					if (telephonyManager.getDeviceId() == null) {
						deviceFP.deviceid = "DeviceIdIsNull";
					}

					IDCardAPI IDCard = new IDCardAPI();

					if (isSIM) {
						deviceFP.deviceid = "DeviceIdIsNull";
					}

					this.ReturnInitIDCard = IDCard.InitIDCard("1002", nTypeInitIDCard,
							this.idcardpath, telephonyManager, deviceFP);
					System.out.println("ReturnInitIDCard：" + this.ReturnInitIDCard);
					this.ReturnAuthority = 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.ReturnInitIDCard = -10003;
			}
		}
	}

	public String readInitFileString(String filePathString) {
		String SysCertVersion = "wtversion5_5";
		String deviceidString = "";
		File dateInitFile = new File(filePathString);
		if (dateInitFile.exists()) {
			try {
				BufferedReader bfReader = new BufferedReader(new FileReader(dateInitFile));
				deviceidString = bfReader.readLine();
				bfReader.close();
				deviceidString = this.common.getSrcPassword(deviceidString, SysCertVersion);
			} catch (FileNotFoundException e) {
				deviceidString = "";
				e.printStackTrace();
			} catch (IOException e) {
				deviceidString = "";
				e.printStackTrace();
			} catch (Exception e) {
				deviceidString = "";
				e.printStackTrace();
			}

		}

		return deviceidString;
	}

	private String readAssetFile(AssetManager am, String filename) {
		String typeModeString = null;
		try {
			InputStream iStream = am.open(filename);
			int size_is = iStream.available();
			byte[] byte_new = new byte[size_is];
			iStream.read(byte_new);
			iStream.close();
			typeModeString = new String(byte_new);
		} catch (IOException e) {
			typeModeString = null;
		} catch (Exception e) {
			typeModeString = null;
		}
		return typeModeString;
	}

	public IBinder onBind(Intent intent) {
		return this.rgBinder;
	}

	public void copyBigFile() throws IOException {
		String[] IDCARDANDROID = { "IDCARDANDROID1.xml", "IDCARDANDROID2.xml", "IDCARDANDROID3.xml" };

		String[] IDCARDANDROIDABROAD = { "IDCARDANDROIDABROAD1.xml", "IDCARDANDROIDABROAD2.xml",
				"IDCARDANDROIDABROAD3.xml" };

		String[] idcls = { "idcls1.lib", "idcls2.lib", "idcls3.lib", "idcls4.lib" };
		String[] pntWTPENPDA = { "pntWTPENPDA1.lib", "pntWTPENPDA2.lib", "pntWTPENPDA3.lib" };

		if (nTypeInitIDCard == 0) {
			mergeFile(IDCARDANDROID, "IDCARDANDROID.xml");
		} else if (nTypeInitIDCard == 3) {
			mergeFile(IDCARDANDROIDABROAD, "IDCARDANDROIDABROAD.xml");
		}
		mergeFile(pntWTPENPDA, "pntWTPENPDA.lib");
	}

	public void copyHolderBigFile() throws IOException {
		String[] IDCARDANDROID = { "IDCARDANDROID1.xml", "IDCARDANDROID2.xml", "IDCARDANDROID3.xml" };

		String[] idcls = { "idcls1.lib", "idcls2.lib", "idcls3.lib", "idcls4.lib" };
		String[] IDCARDANDROIDABROAD = { "IDCARDANDROIDABROAD1.xml", "IDCARDANDROIDABROAD2.xml",
				"IDCARDANDROIDABROAD3.xml" };

		String[] pntWTPENPDA = { "pntWTPENPDA1.lib", "pntWTPENPDA2.lib" };

		mergeFile(idcls, "idcls.lib");
		mergeFile(IDCARDANDROID, "IDCARDANDROID.xml");
		mergeFile(pntWTPENPDA, "pntWTPENPDA.lib");
		mergeFile(IDCARDANDROIDABROAD, "IDCARDANDROIDABROAD.xml");
	}

	public void mergeFile(String[] file, String filename) throws IOException {
		Common common = new Common();
		String filepath = common.getSDPath() + "/AndroidWT/IDCard/" + filename;

		File newfile = new File(filepath);
		if ((newfile != null) && (newfile.exists()) && (newfile.isFile())) {
			newfile.delete();
		}
		OutputStream out = new FileOutputStream(filepath);
		byte[] buffer = new byte[1024];

		int readLen = 0;
		for (int i = 0; i < file.length; ++i) {
			InputStream in = getAssets().open(file[i]);
			while ((readLen = in.read(buffer)) != -1) {
				out.write(buffer, 0, readLen);
			}
			out.flush();
			in.close();
		}

		out.close();
	}

	public void copyDataBase() throws IOException {
		Common common = new Common();
		String dst = common.getSDPath() + "/AndroidWT/IDCard/";
		String[] filename = { "Special.txt", "OEMtest.txt", "ProvName.txt",
				"IDCLASSIFIERANDROID.xml", "THOCR_pspt.lib", "THOCR_LP.lib",
				"thocr_Driver_License.lib", "IssueAndBirth.txt", "THOCR_Num_Char.lib",
				"BrandModel.txt", "version.txt", "wtdate.lsc", "wtversion.lsc" };
		for (int i = 0; i < filename.length; ++i) {
			String outFileName = dst + filename[i];

			File file = new File(dst);
			if (!(file.exists())) {
				file.mkdirs();
			}
			file = new File(outFileName);
			if (file.exists()) {
				file.delete();
			}
			try {
				InputStream myInput = getAssets().open(filename[i]);
				OutputStream myOutput = new FileOutputStream(outFileName);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = myInput.read(buffer)) > 0) {
					myOutput.write(buffer, 0, length);
				}
				myOutput.flush();
				myOutput.close();
				myInput.close();
			} catch (Exception e) {
				System.out.println(filename[i] + "is not found");
			}
		}
	}

	private String readSDCardFile() {
		String authModeContent = null;
		try {
			File idcardfile = new File(this.idcardpath);
			if (!(idcardfile.exists()))
				return null;
			File file = new File(this.idcardpath + "authmode.lsc");
			if (file.exists()) {
				FileInputStream fileInputStream = new FileInputStream(this.idcardpath
						+ "authmode.lsc");
				int size_is = fileInputStream.available();
				byte[] byte_new = new byte[size_is];
				fileInputStream.read(byte_new);
				fileInputStream.close();
				authModeContent = new String(byte_new);
			}
		} catch (Exception e) {
			authModeContent = null;
		}
		return authModeContent;
	}

	public class recogBinder extends Binder {
		IDCardAPI IDCard;

		public recogBinder() {
			this.IDCard = new IDCardAPI();
		}

		public ResultMessage getRecogResult(RecogParameterMessage rpm) throws Exception {
			if (rpm != null) {
				int ReturnAuthority = IDCardAuthAndInit(rpm);
//				int ReturnAuthority = 0;
//				RecogService.this.ReturnInitIDCard = 0;
				if ((RecogService.this.ReturnInitIDCard == 0) && (ReturnAuthority == 0)) {
					System.out.println("裁切++");
					if (rpm.nMainID == 1036) {
						IDCardLoadAndRecogMRZ_TY(rpm);
					} else if (rpm.nMainID == 1034) {
						IDCardLoadAndRecogMRZ_TY(rpm);
					} else if (rpm.nMainID == 1033) {
						IDCardLoadAndRecogThreeLineMRZ(rpm);
					} else if (rpm.nMainID == 1020) {
						IDCardLoadAndRecogMRZ(rpm);
					} else {
						if ((((rpm.nMainID == 1100) || (rpm.nMainID == 1101)))
								&& (!(rpm.isAutoRecog))) {
							IDCardLoadAndCutLineationImage(rpm);
						} else if (rpm.nMainID == 1102) {
							IDCardLoadNoLineationImage(rpm);
						} else {
							IDCardLoadNoLineationImage(rpm);
							IDCardCutNoLineationImage(rpm);
						}

						IDCardRecognitionImage(rpm);
					}

					IDCardGetRecognitionResult(rpm);
					resultMessage = IDCardReturnRecognitionResult(rpm);

					return resultMessage;
				}
				ResultMessage resultMessage = new ResultMessage();
				resultMessage.ReturnAuthority = IDCardAuthAndInit(rpm);
				resultMessage.ReturnInitIDCard = RecogService.this.ReturnInitIDCard;
				return resultMessage;
			}

			return null;
		}

		public int IDCardAuthAndInit(RecogParameterMessage rpm) {
			RecogService.this.resultMessage = new ResultMessage();
			if (RecogService.this.ReturnAuthority != 0) {
				return RecogService.this.ReturnAuthority;
			}
			boolean typebool = false;
			if (rpm.nMainID == 0) {
				String cfg = "";
				try {
					cfg = RecogService.this.readtxt();
				} catch (Exception e) {
					e.printStackTrace();
				}
				String[] cfgs = cfg.split("==##");
				if ((cfgs != null) && (cfgs.length >= 2)) {
					rpm.nMainID = RecogService.this.String2Int(cfgs[0]);
				}
			}
			if (rpm.nMainID == 0) {
				rpm.nMainID = 2;
			}
			String nMainIDString = rpm.nMainID + "";
			String type_result_string = null;
			String[] resultStrings = (String[]) null;
			String SysCertVersion = "wtversion5_5";
			try {
				InputStream iStream = RecogService.this.getAssets().open("authtype.lsc");
				int size_is = iStream.available();
				byte[] byte_new = new byte[size_is];
				iStream.read(byte_new);
				iStream.close();
				type_result_string = new String(byte_new);
				type_result_string = RecogService.this.common.getSrcPassword(type_result_string,
						SysCertVersion);
				resultStrings = type_result_string.split(";");
			} catch (IOException e1) {
				resultStrings = (String[]) null;
			} catch (Exception e) {
				resultStrings = (String[]) null;
			}

			if (resultStrings != null) {
				for (int i = 0; i < resultStrings.length; ++i) {
					if (resultStrings[i].equals(nMainIDString)) {
						typebool = true;
					}
				}
			}
			typebool = true;
			if (!(typebool)) {
				RecogService.this.resultMessage.ReturnAuthority = -10016;
				return RecogService.this.resultMessage.ReturnAuthority;
			}

			int devCheck = -10400;
			if ((RecogService.this.mafr.isCheckDevType("11")) || (rpm.isCheckDevType))
				devCheck = RecogService.this.mafr.isAllowDevTypeAndDevCode("11", rpm.devcode);
			else
				devCheck = 0;
			String originalAuthFilePath = "";
			CDKey cdKey = null;
			boolean fleg;
			String oldwtlsc = "";
			if ((rpm.versionfile != null) && (!(rpm.versionfile.equals("")))) {
				if (rpm.versionfile.equals("assets"))
					rpm.versionfile = Environment.getExternalStorageDirectory().toString()
							+ "/AndroidWT/IDCard/wtversion.lsc";
				VersionAuthFileOperate vafo = new VersionAuthFileOperate();
				RecogService.this.resultMessage.ReturnAuthority = vafo.verifyVersionAuthFile(
						rpm.versionfile, rpm.devcode, "11", rpm.nMainID + "");
			} else if ((rpm.dateFilePath != null) && (!(rpm.dateFilePath.equals("")))) {
				File dateFile = new File(rpm.dateFilePath);
				if (dateFile.exists()) {
					Boolean bool = DateAuthFileOperate.judgeDateAuthFileBoolean(rpm.dateFilePath);
					DateAuthFileOperate dafo = new DateAuthFileOperate();
					if (bool.booleanValue()) {
						RecogService.this.resultMessage.ReturnAuthority = dafo.verifyDateAuthFile(
								rpm.dateFilePath, rpm.devcode, "11");
						if (RecogService.this.resultMessage.ReturnAuthority == -10090)
							RecogService.this.resultMessage.ReturnAuthority = 0;
					} else {
						RecogService.this.resultMessage.ReturnAuthority = dafo
								.verifyOldDateAuthFile(rpm.dateFilePath, rpm.devcode);
						if (RecogService.this.resultMessage.ReturnAuthority == -10090)
							RecogService.this.resultMessage.ReturnAuthority = 0;
					}
				}
			} else if (RecogService.this.isTF.booleanValue()) {
				RecogService.this.resultMessage.ReturnAuthority = 0;
			} else if (RecogService.this.mafr.isCheckPRJMode("11")) {
				String packageName = RecogService.this.getPackageName();
				PackageInfo pkg = null;
				try {
					pkg = RecogService.this.getPackageManager().getPackageInfo(
							RecogService.this.getApplication().getPackageName(), 0);
				} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
				}
				String app_name = pkg.applicationInfo.loadLabel(
						RecogService.this.getPackageManager()).toString();
				String company_name = null;
				try {
					//2130968577
					int id_company_name = RecogService.this.getResources().getIdentifier(
							"company_name", "string", RecogService.this.getPackageName());
					company_name = RecogService.this.getResources().getString(id_company_name);
					//北京文通科技有限公司
				} catch (Resources.NotFoundException e) {
					e.printStackTrace();
					Toast.makeText(RecogService.this.getApplicationContext(),
							"在strings文件中未找到company_name字段", 1).show();
				}
				if ((app_name != null) && (company_name != null)) {
					RecogService.this.resultMessage.ReturnAuthority = RecogService.this.mafr
							.isCheckPRJOK("11", rpm.devcode, packageName, app_name, company_name);
					RecogService.this.resultMessage.ReturnAuthority = -10090;
					if ((RecogService.this.resultMessage.ReturnAuthority == -10090)
							&& (devCheck == 0))
						RecogService.this.resultMessage.ReturnAuthority = 0;
				}
			} else {
				ProcedureAuthOperate pao = new ProcedureAuthOperate(
						RecogService.this.getApplicationContext());
				originalAuthFilePath = pao.getOriginalAuthFilePathByProjectType("11");
				File originalAuthFile = new File(originalAuthFilePath);
				String[] str = new String[12];
				cdKey = new CDKey();
				fleg = false;
				if (originalAuthFile.exists()) {
					try {
						str = pao.readOriginalAuthFileContent(originalAuthFilePath);
					} catch (Exception e) {
						str[1] = "";
						str[2] = "";
					}

					fleg = cdKey.checkjhm(str[2], RecogService.this.mcode, str[1]);

					if ((!(fleg)) && (str.length >= 8) && (str[8] != null) && (str[7] != null)) {
						fleg = cdKey.checkjhm(str[8], RecogService.this.mcode, str[7]);
					}
					if (fleg) {
						RecogService.this.resultMessage.ReturnAuthority = 0;
						RecogService.this.resultMessage.ReturnInitIDCard = RecogService.this.ReturnInitIDCard;
					} else {
						RecogService.this.resultMessage.ReturnAuthority = -10015;
						RecogService.this.resultMessage.ReturnInitIDCard = RecogService.this.ReturnInitIDCard;
					}
					return RecogService.this.resultMessage.ReturnAuthority;
				}

				SqliteHelperUtils sqliteHelperUtils = new SqliteHelperUtils(
						RecogService.this.getApplicationContext(), "wt.db", 2);
				oldwtlsc = sqliteHelperUtils.queryData(RecogService.query_old_lsc, null);
				if ((oldwtlsc == null) || (oldwtlsc.equals("")))
					;
			}
			try {
				Common common = new Common();

				String aesjie = common.getSrcPassword(oldwtlsc, SysCertVersion);
				String[] result = aesjie.split(",");
				fleg = cdKey.checkjhm(result[2], RecogService.this.mcode, result[1]);

				File originalAuthRootFile = new File(Environment.getExternalStorageDirectory()
						.toString() + "/wintone/");
				if (!(originalAuthRootFile.exists()))
					originalAuthRootFile.mkdirs();
				FileOutputStream outStream = new FileOutputStream(originalAuthFilePath);
				outStream.write(oldwtlsc.getBytes());
				outStream.close();
				if (fleg) {
					RecogService.this.resultMessage.ReturnAuthority = 0;
					RecogService.this.resultMessage.ReturnInitIDCard = RecogService.this.ReturnInitIDCard;
				} else {
					RecogService.this.resultMessage.ReturnAuthority = -10015;
					RecogService.this.resultMessage.ReturnInitIDCard = RecogService.this.ReturnInitIDCard;
				}
			} catch (Exception e) {
			}
			RecogService.this.resultMessage.ReturnAuthority = 0;
			RecogService.this.resultMessage.ReturnInitIDCard = RecogService.this.ReturnInitIDCard;

			return RecogService.this.resultMessage.ReturnAuthority;
		}

		public void IDCardLoadAndRecogMRZ_TY(RecogParameterMessage rpm) {
			if (rpm.GetVersionInfo) {
				RecogService.this.resultMessage.ReturnGetVersionInfo = this.IDCard.GetVersionInfo();
			}

			if (!(rpm.lpFileName.equals("")))
				RecogService.this.resultMessage.ReturnLoadImageToMemory = this.IDCard
						.LoadImageToMemory(rpm.lpFileName, rpm.nTypeLoadImageToMemory);
			else {
				RecogService.this.resultMessage.ReturnLoadImageToMemory = 0;
			}
			System.out.println("resultMessage.ReturnLoadImageToMemory:"
					+ RecogService.this.resultMessage.ReturnLoadImageToMemory);
			if (RecogService.this.resultMessage.ReturnLoadImageToMemory == 0)
				switch (rpm.nMainID) {
				case 1034:
					if (!(rpm.lpFileName.equals(""))) {
						RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard
								.RecogMRZEx();
						return;
					}
					RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard
							.RecogNV21ImageEx(rpm.nv21bytes, rpm.width, rpm.height, rpm.left,
									rpm.right, rpm.top, rpm.bottom, rpm.nRotateType, 1034, 0);

					break;
				case 1036:
					if (!(rpm.lpFileName.equals(""))) {
						RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard
								.RecogTwoLines36MRZ();

						return;
					}

					RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard
							.RecogNV21ImageEx(rpm.nv21bytes, rpm.width, rpm.height, rpm.left,
									rpm.right, rpm.top, rpm.bottom, rpm.nRotateType, 1036, 0);
				case 1035:
				}
		}

		public void IDCardLoadAndRecogThreeLineMRZ(RecogParameterMessage rpm) {
			if (!(rpm.lpFileName.equals("")))
				RecogService.this.resultMessage.ReturnLoadImageToMemory = this.IDCard
						.LoadImageToMemory(rpm.lpFileName, rpm.nTypeLoadImageToMemory);
			else
				RecogService.this.resultMessage.ReturnLoadImageToMemory = 0;
			if (RecogService.this.resultMessage.ReturnLoadImageToMemory != 0)
				return;
			if (!(rpm.lpFileName.equals("")))
				RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard
						.RecogThreeLinesMRZ();
			else
				RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard.RecogNV21ImageEx(
						rpm.nv21bytes, rpm.width, rpm.height, rpm.left, rpm.right, rpm.top,
						rpm.bottom, rpm.nRotateType, 1033, 0);
		}

		public void IDCardLoadAndRecogMRZ(RecogParameterMessage rpm) {
			if (rpm.GetVersionInfo) {
				RecogService.this.resultMessage.ReturnGetVersionInfo = this.IDCard.GetVersionInfo();
			}

			Date dateLoadImageToMemory1 = new Date();
			RecogService.this.resultMessage.ReturnLoadImageToMemory = this.IDCard
					.LoadImageToMemory(rpm.lpFileName, rpm.nTypeLoadImageToMemory);
			Date dateLoadImageToMemory2 = new Date();
			Log.i("TimeTAG", "LoadImageToMemory="
					+ (dateLoadImageToMemory2.getTime() - dateLoadImageToMemory1.getTime()));

			if (RecogService.this.resultMessage.ReturnLoadImageToMemory != 0)
				return;
			RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard.RecogMRZ(rpm.array,
					rpm.ncheckmrz);
		}

		public String IDCardLoadAndCutLineationImage(RecogParameterMessage rpm) {
			if (rpm.GetVersionInfo) {
				RecogService.this.resultMessage.ReturnGetVersionInfo = this.IDCard.GetVersionInfo();
			}
			int isProcessImage = -1001;
			RecogService.this.resultMessage.lpFileOut = rpm.lpFileName + ".jpg";

			if (rpm.nMainID == 1100)
				isProcessImage = this.IDCard.GetRectPosVehicleNum(rpm.lpFileName, rpm.array,
						rpm.multiRows, RecogService.this.resultMessage.lpFileOut);
			else {
				isProcessImage = this.IDCard.GetRectPos(rpm.lpFileName, rpm.array, rpm.multiRows,
						RecogService.this.resultMessage.lpFileOut);
			}
			if (isProcessImage == 0) {
				rpm.lpFileName = RecogService.this.resultMessage.lpFileOut;
				RecogService.this.resultMessage.lpFileName = RecogService.this.resultMessage.lpFileOut;
				RecogService.this.resultMessage.isProcessImage = isProcessImage;
				RecogService.this.resultMessage.ReturnLoadImageToMemory = this.IDCard
						.LoadImageToMemory(rpm.lpFileName, rpm.nTypeLoadImageToMemory);
			}
			return RecogService.this.resultMessage.lpFileName;
		}

		public void IDCardLoadNoLineationImage(RecogParameterMessage rpm) {
			if (rpm.GetVersionInfo) {
				RecogService.this.resultMessage.ReturnGetVersionInfo = this.IDCard.GetVersionInfo();
			}
			if (RecogService.isRecogByPath) {
				RecogService.this.resultMessage.ReturnLoadImageToMemory = this.IDCard
						.LoadImageToMemory(rpm.lpFileName, rpm.nTypeLoadImageToMemory);
			} else {
				RecogService.this.resultMessage.ReturnLoadImageToMemory = this.IDCard
						.LoadBufferImageAndroid(rpm.nv21bytes, rpm.nv21_width, rpm.nv21_height, 24);

				File file = new File(rpm.lpFileName.substring(0, rpm.lpFileName.lastIndexOf("/")));
				if (!(file.exists()))
					file.mkdirs();
				this.IDCard.SaveImage(rpm.lpFileName);
			}
		}

		public void IDCardCutNoLineationImage(RecogParameterMessage rpm) {
			if (RecogService.this.resultMessage.ReturnLoadImageToMemory == 0) {
				int iProcessImage = -1;
				int degreeImahe = -1001;
				if ((rpm.isCut)
						&& (((rpm.nMainID == 13) || (rpm.nMainID == 2) || (rpm.nMainID == 5)
								|| (rpm.nMainID == 6) || (rpm.nMainID == 9) || (rpm.nMainID == 11)
								|| (rpm.nMainID == 12) || (rpm.nMainID == 22)
								|| (rpm.nMainID == 1001) || (rpm.nMainID == 1005)
								|| (rpm.nMainID == 14) || (rpm.nMainID == 15)
								|| (rpm.nMainID == 10) || (rpm.nMainID == 1030)
								|| (rpm.nMainID == 1031) || (rpm.nMainID == 1032)
								|| (rpm.nMainID == 2001) || (rpm.nMainID == 2004)
								|| (rpm.nMainID == 2003) || (rpm.nMainID == 2002)))) {
					System.out.println("rpm.triggertype:" + rpm.triggertype);
					if (rpm.triggertype == 0) {
						if (rpm.nMainID == 2) {
							degreeImahe = this.IDCard.AutoRotateImage(1);
						} else {
							degreeImahe = this.IDCard.AutoRotateImage(3);
						}

						iProcessImage = this.IDCard.AutoCropImage(rpm.nMainID);
						System.out.println("裁剪完毕");
					} else {
						iProcessImage = this.IDCard.SpecialAutoCropImageExt(0);

						degreeImahe = this.IDCard.AutoRotateImage(2);
						System.out.println("支架裁切");
					}
				}

				if (rpm.isSaveCut) {
					String cutSavePath = rpm.lpFileName + ".jpg";
					this.IDCard.SaveImage(cutSavePath);
				}
			}
		}

		public void IDCardRecognitionImage(RecogParameterMessage rpm) {
			if (RecogService.this.resultMessage.ReturnLoadImageToMemory == 0) {
				int[] nSubID;
				if ((rpm.isAutoClassify) && (rpm.nMainID == 3000)) {
					nSubID = new int[4];
					nSubID[0] = 0;
					this.IDCard.SetIDCardID(2006, nSubID);
					this.IDCard.AddIDCardID(2007, nSubID);
					this.IDCard.AddIDCardID(2008, nSubID);

					System.out.println("返回识别值3000");
					this.IDCard.SpecialAutoCropImageExt(3);
					this.IDCard.ProcessImage(2);
					String cutSavePath = rpm.lpFileName + ".jpg";
					this.IDCard.SaveImage(cutSavePath);
					RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard.RecogIDCard();
					System.out.println("resultMessage.ReturnRecogIDCard:"
							+ RecogService.this.resultMessage.ReturnRecogIDCard);
				} else if ((rpm.isAutoClassify) && (rpm.nMainID != 3000)) {
					nSubID = new int[4];
					nSubID[0] = 0;
					if (rpm.isOnlyClassIDCard) {
						this.IDCard.SetIDCardID(2, nSubID);
						this.IDCard.AddIDCardID(3, nSubID);
					} else {
						this.IDCard.SetIDCardID(2, nSubID);

						this.IDCard.AddIDCardID(5, nSubID);
						this.IDCard.AddIDCardID(6, nSubID);
						this.IDCard.AddIDCardID(9, nSubID);
						this.IDCard.AddIDCardID(10, nSubID);
						this.IDCard.AddIDCardID(11, nSubID);
						this.IDCard.AddIDCardID(12, nSubID);
						this.IDCard.AddIDCardID(13, nSubID);

						this.IDCard.AddIDCardID(22, nSubID);
					}

					RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard.RecogIDCard();
				} else {
					int SubID = 0;
					if ((rpm.nSubID != null) && (rpm.nSubID.length > 0)) {
						SubID = rpm.nSubID[0];
					}

					if (rpm.nMainID == 1102) {
						RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard
								.RecogIDNumber();
					} else {
						System.out.println("开始识别");

						RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard
								.RecogIDCardEX(rpm.nMainID, SubID);
					}
				}
			}
		}

		public void IDCardGetRecognitionResult(RecogParameterMessage rpm) {
			if (rpm.GetSubID) {
				RecogService.this.resultMessage.ReturnGetSubID = this.IDCard.GetSubId();
			}

			if ((rpm.lpHeadFileName != null) && (!(rpm.lpHeadFileName.equals(""))))
				RecogService.this.resultMessage.ReturnSaveHeadImage = this.IDCard
						.SaveHeadImage(rpm.lpHeadFileName);
		}

		public ResultMessage IDCardReturnRecognitionResult(RecogParameterMessage rpm) {
			List listDate = new ArrayList();
			listDate.clear();
			RecogService.this.resultMessage.ReturnAuthority = 0;
			if (RecogService.this.resultMessage.ReturnAuthority != 0) {
				ResultMessage errorResultMessage = new ResultMessage();
				errorResultMessage.ReturnAuthority = RecogService.this.resultMessage.ReturnAuthority;
				errorResultMessage.ReturnInitIDCard = RecogService.this.resultMessage.ReturnInitIDCard;
				return errorResultMessage;
			}

			for (int i = 0; i < 20; ++i) {
				String buffer = "";
				int[] textNamePosition = new int[4];
				buffer = this.IDCard.GetFieldName(i);
				RecogService.this.resultMessage.GetFieldName[i] = buffer;

				if (buffer == null)
					break;
				buffer = this.IDCard.GetRecogResult(i);
				RecogService.this.resultMessage.GetRecogResult[i] = buffer;
				if (!(rpm.isGetRecogFieldPos))
					continue;
				this.IDCard.GetRecogFieldPos(i, textNamePosition);
				listDate.add(textNamePosition);
			}

			RecogService.this.resultMessage.textNamePosition = listDate;
			return RecogService.this.resultMessage;
		}

		public int IDCardGetInit() {
			return RecogService.this.ReturnInitIDCard;
		}
	}
}