package navkon.maps;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

public class mojaLokacja extends Service implements LocationListener {
private mojaMapa mapa; 
private int officeId;
private int modeId;
private double szerokosc;
private getDirection directions;
private double dlugosc;
private float kierunek;
private float odleglosc;
private float predkosc;
private double ostatniaSzerokosc;
private double ostatniaDlugosc;
private Context context; 
private boolean czyPierwsza = true;
public boolean czySaDane = false;
public List<LatLng> checkPoints;
public List<String> htmlInstructions;
private String poczatkowyAdres;
private String koncowyAdres;
private String odlPrzewidywana;
private String czasPrzewidywany;
private int steps;
private int thisStep;
private LatLng waypoint;
private int infoStepsShowed;
private boolean isBearing = false;
private boolean showedFirst = false;
ActivityMain main;


public mojaLokacja(mojaMapa mapa, Context context, int officeId, int modeId, ActivityMain main, LatLng waypoint)
{
	this.mapa = mapa;
	this.context = context;
	this.thisStep = 0;
	this.infoStepsShowed=0;
	this.officeId = officeId;
	this.modeId = modeId;
	this.main = main;
	this.predkosc= (float)0.0;
	this.waypoint = waypoint;
	
}
	@Override
	public void onLocationChanged(Location wsp) {
		//Pobierz ostatnie wspolrzedne
		ostatniaDlugosc = dlugosc;              
		ostatniaSzerokosc = szerokosc;
		//Update wspó³rzednych
		szerokosc = wsp.getLatitude();
		dlugosc = wsp.getLongitude();
		predkosc = wsp.getSpeed();
		main.updateSpeed(predkosc);
		//Jesli pierwsza wspo³rzêdna przyjmij takie same
		if(czyPierwsza)
		{
			ostatniaSzerokosc = szerokosc;
			ostatniaDlugosc = dlugosc;
		    this.directions = new getDirection(context,szerokosc,dlugosc,mapa,this, officeId, modeId, waypoint);
			directions.execute();
			czyPierwsza = false;
		}
		if(czySaDane)
		{
			if(!showedFirst)
			{
				main.showInfo(htmlInstructions.get(0));
				showedFirst=true;
				infoStepsShowed++;
			}
			if(!isBearing)
			{
				obliczKierunek(thisStep);		
				isBearing = true;
			}
			if (obliczOdl(szerokosc,dlugosc,checkPoints.get(thisStep).latitude,checkPoints.get(thisStep).longitude)<20&&infoStepsShowed == thisStep+1)
			{
				if(infoStepsShowed == steps)
				{
					main.showInfo(context.getResources().getString(R.string.reached));
				}
				else
				{
					main.showInfo(htmlInstructions.get(infoStepsShowed));
					if (infoStepsShowed<steps)
					{infoStepsShowed++;}
				}
			}
			if (obliczOdl(szerokosc,dlugosc,checkPoints.get(thisStep).latitude,checkPoints.get(thisStep).longitude)<5.0)
			{
				onCheckpointReach();
			}
		}
		mapa.setCameraOn(pobierzLokacje(),kierunek);
		mapa.setPosition(pobierzLokacje());
		odleglosc += obliczOdl(szerokosc, dlugosc, ostatniaSzerokosc, ostatniaDlugosc);
		main.updateDistance(odleglosc);
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public LatLng pobierzLokacje()
	{
		return new LatLng(this.szerokosc,this.dlugosc);
	}

	
	private float obliczOdl(double szerokosc, double dlugosc, double lat, double lng)
	{
		float res[] = new float[3];
		Location.distanceBetween(szerokosc, dlugosc, lat, lng, res);
		return res[0];
	}
	private void obliczKierunek(int checkpointId)
	{
		float res[] = new float[3];
		Location.distanceBetween(szerokosc, dlugosc, checkPoints.get(checkpointId).latitude, checkPoints.get(checkpointId).longitude, res);
		if (res[1]!=(float)0.0)
		{
			this.kierunek = res[1];
		}
		else
		{
			this.kierunek = res[2];
		}
	}
	public void setInfo(String distance, String duration, String startAddress, String endAddress, int numberOfSteps)
	{
		this.odlPrzewidywana = distance;
		this.czasPrzewidywany=duration;
		this.poczatkowyAdres = startAddress;
		this.koncowyAdres = endAddress;
		this.steps = numberOfSteps;
		this.czySaDane = true;
	}
	public String pobierzDystans()
	{
		return this.odlPrzewidywana;
	}
	public String pobierzCzas()
	{
		return this.czasPrzewidywany;
	}
	public String pobierzAdrPocz()
	{
		return this.poczatkowyAdres;
	}
	public String pobierzAdrKonc()
	{
		return this.koncowyAdres;
	}
	private void onCheckpointReach()
	{
		if (czySaDane && thisStep<steps-1)
		{
			thisStep++;
			obliczKierunek(thisStep);
		}
	}
}
