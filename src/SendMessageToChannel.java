public class SendMessageToChannel implements Runnable{

	private String nameChannel;
	private byte[] message;

	public SendMessageToChannel(String nameChannel, byte[] message){
		this.nameChannel= nameChannel;
		this.message = message;
    Client.setAuthenticationChannel();
	}

	@Override
	public void run(){
		try{
		switch(nameChannel){

			case "authentication":
				Client.sendAuthentication(message);
			break;

			default:
			System.out.println("Invalid channel.");
			break;
		};


		}catch(Exception ex){
				ex.printStackTrace();
				}


	}

}
