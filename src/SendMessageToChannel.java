public class SendMessageToChannel implements Runnable{

	private String nameChannel;
	private byte[] message;

	public SendMessageToChannel(String nameChannel, byte[] message){
		this.nameChannel= nameChannel;
		this.message = message;
	}

	@Override
	public void run(){
		try{
		switch(nameChannel){

			case "authentication":
				Client.setAuthenticationChannel();
				Client.sendAuthentication(message);
			break;

			case "owner":
				Client.setOwnerChannel();
				Client.sendOwner(message);
			break;

			case "joinTravel":
				Client.setJoinTravelChannel();
				Client.sendJoinTravel(message);
			break;

			case "list":
				Client.setListChannel();
				Client.sendList(message);
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
