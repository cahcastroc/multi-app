package me.camila.aula2intent


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast


class MainActivity : AppCompatActivity() {


    val CAMERA_PERMISSION_CODE = 100
    val CAMERA_REQUEST = 1888  //Cód.intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //---------------------------- Discagem

        val btnFone = this.findViewById<Button>(R.id.btnFone)
        val etTexto = this.findViewById<EditText>(R.id.etTexto)

        btnFone.setOnClickListener {
            val callIntent: Intent = Uri.parse("tel: ${etTexto.text.toString()}").let { number ->
                Intent(Intent.ACTION_DIAL, number)
            }
            this.startActivity(callIntent)
        }

        //---------------------- MAPA

        val btMaps = this.findViewById<Button>(R.id.btMaps)
        btMaps.setOnClickListener {
            val mapsIntent: Intent =
                Uri.parse("geo:0,0?q=${etTexto.text.toString()}").let { local ->
                    Intent(Intent.ACTION_VIEW, local)
                }
            this.startActivity(mapsIntent)
        }

        //-------------------- Compartilhamento

        val btShare = this.findViewById<Button>(R.id.btShare)
        btShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain") //Indica texto simples
            shareIntent.putExtra(Intent.EXTRA_TEXT, etTexto.text.toString())

            val chooser =
                Intent.createChooser(shareIntent, "Selecione uma Opção de Compartilhamento")
            this.startActivity(chooser)
        }

        //-------------------Câmera

        val btCamera = this.findViewById<Button>(R.id.btCamera)

        btCamera.setOnClickListener {

             if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

            //Solicita permissão
                this.requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            } else {
            //Chama função que executa o Intent da Câmera
                openCamera()
            }
        }
    }

    // Confere permissão

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == CAMERA_PERMISSION_CODE){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera()

            }else{
                 Toast.makeText(this, "Não permitido acesso à câmera", Toast.LENGTH_LONG).show()
            }
        }
    }

    //Retorno da câmera

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){

            val picture = data?.extras!!["data"] as Bitmap
            val imgCamera = this.findViewById<ImageView>(R.id.imgCamera)
            imgCamera.setImageBitmap(picture)
        }
    }

    private fun openCamera(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        this.startActivityForResult(cameraIntent,CAMERA_REQUEST)
    }
}


